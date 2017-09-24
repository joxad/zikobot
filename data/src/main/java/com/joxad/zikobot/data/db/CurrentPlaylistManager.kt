package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoTrack
import com.joxad.zikobot.data.db.model.ZikoTrack_Table
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Select
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by Jocelyn on 14/08/2017.
 */

enum class CurrentPlaylistManager {
    INSTANCE;

    private var currentIndex: Int = 0
    var currentTrack: ZikoTrack? = null
    private lateinit var refreshSubject: PublishSubject<ZikoTrack>

    fun init() {
        refreshSubject = PublishSubject.create()
        currentTrack = Select().from(ZikoTrack::class.java).where(ZikoTrack_Table.zikoPlaylist_id.eq(1))
                .limit(1).orderBy(ZikoTrack_Table.id, false).querySingle()
        if (currentTrack == null)
            currentTrack = ZikoTrack.empty()
    }

    fun play(track: ZikoTrack) {
        val result = Select().from(ZikoTrack::class.java).where(ZikoTrack_Table.ref.eq(track.ref))
                .and(ZikoTrack_Table.zikoPlaylist_id.eq(1)).querySingle()
        if (result == null) {
            val database = FlowManager.getDatabase(ZikoDB::class.java)
            val transaction = database.beginTransactionAsync({
                val zikoTrack = ZikoTrack.trackToPlay(track)
                zikoTrack.save()
                currentTrack = zikoTrack
                currentIndex = getTracks().size
                refreshSubject.onNext(currentTrack!!)
            }).build()
            transaction.execute()
        } else {
            currentTrack = result
            for ((index, t) in getTracks().withIndex()) {
                if (t.id == currentTrack!!.id) {
                    currentIndex = index
                    break
                }
            }

            refreshSubject.onNext(currentTrack!!)

        }
    }

    fun previous() {
        currentIndex--
        if (currentIndex < 0)
            currentIndex = 0
        val zikoTrack = getTracks()[currentIndex]
        play(zikoTrack)
    }

    fun next() {
        currentIndex++
        var zikoTrack: ZikoTrack
        try {
            zikoTrack = getTracks()[currentIndex]
        } catch (exception: IndexOutOfBoundsException) {
            currentIndex = 0
            zikoTrack = getTracks()[currentIndex]
        }
        play(zikoTrack)
    }

    fun getTracks(): List<ZikoTrack> {
        return PlaylistManager.INSTANCE.findPlayingPlaylist()!!.getForeignTracks()
    }

    fun addTrack(track: ZikoTrack) {
        val result = Select().from(ZikoTrack::class.java).where(ZikoTrack_Table.ref.eq(track.ref))
                .and(ZikoTrack_Table.zikoPlaylist_id.eq(1)).querySingle()
        if (result == null) {
            val zikoTrack = ZikoTrack.trackToPlay(track)
            zikoTrack.save()
        }
    }

    fun play(tracks: List<ZikoTrack>) {

        val oldHisto = Select().from(ZikoTrack::class.java).where(ZikoTrack_Table.zikoPlaylist_id.eq(1)).queryList()
        val database = FlowManager.getDatabase(ZikoDB::class.java)

        val transaction = database.beginTransactionAsync({
            for (oldTrack in oldHisto) {
                oldTrack.delete()
            }
            for (zTrack in tracks) {
                addTrack(zTrack)
            }
            val result = Select().from(ZikoTrack::class.java)
                    .where(ZikoTrack_Table.zikoPlaylist_id.eq(1)).limit(1).querySingle()
            currentTrack = result
            refreshSubject.onNext(currentTrack!!)

        }).build()
        transaction.execute()

    }

    fun subjectObservable(): Observable<ZikoTrack> {
        return refreshSubject
    }
}
