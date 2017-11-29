package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoAlbum
import com.joxad.zikobot.data.db.model.ZikoArtist
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.joxad.zikobot.data.db.model.ZikoTrack_Table
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.rx2.language.RXModelQueriableImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Jocelyn on 23/10/2017.
 */
object TrackManager {


    fun findOne(id: Long): RXModelQueriableImpl<ZikoTrack> {
        return (select.from(ZikoTrack::class.java)
                .where(ZikoTrack_Table.id.eq(id))
                .rx())
    }


    fun findTopTracksFromAPI(artistId: String): Observable<List<ZikoTrack>> {
        return SpotifyApiManager.INSTANCE.getTopTracks(artistId).flatMap {
            val list = arrayListOf<ZikoTrack>()
            if (it.tracks != null)
                for (track in it.tracks) {
                    val zikoArtist = ZikoArtist.spotify(track?.artists?.get(0)!!)
                    zikoArtist.save()
                    list.add(ZikoTrack.spotify(track, zikoArtist, ZikoAlbum.Companion.spotify(track.album!!), null))
                }
            return@flatMap Observable.just(list)
        }
    }

    fun findTracksFromAPI(album: ZikoAlbum): Observable<List<ZikoTrack>> {
        return SpotifyApiManager.INSTANCE.getAlbumTracks(album.spotifyId, 50, 0).flatMap {
            val list = arrayListOf<ZikoTrack>()
            for (track in it.tracks!!) {
                val zikoArtist = ZikoArtist.spotify(track?.artists?.get(0)!!)
                zikoArtist.save()
                list.add(ZikoTrack.spotify(track, zikoArtist, album, null))
            }
            return@flatMap Observable.just(list).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}