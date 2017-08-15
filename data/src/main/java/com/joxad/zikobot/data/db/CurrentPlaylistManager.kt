package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoTrack
import com.joxad.zikobot.data.db.model.ZikoTrack_Table
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Select
import io.reactivex.subjects.PublishSubject

/**
 * Created by Jocelyn on 14/08/2017.
 */

enum class CurrentPlaylistManager {
    INSTANCE;

    var currentTrack: ZikoTrack? = null
    lateinit var refreshSubject: PublishSubject<ZikoTrack>

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
                refreshSubject.onNext(currentTrack!!)
            }
            ).build()
            transaction.execute()
        } else {
            currentTrack = result
            refreshSubject.onNext(currentTrack!!)

        }
    }

    fun getTracks(): List<ZikoTrack> {
        return PlaylistManager.INSTANCE.findPlayingPlaylist()!!.getForeignTracks()
    }

}
