package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoPlaylist
import com.joxad.zikobot.data.db.model.ZikoPlaylist_Table
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyUser
import com.raizlabs.android.dbflow.annotation.Collate
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.rx2.language.RXModelQueriableImpl
import com.raizlabs.android.dbflow.sql.language.OrderBy
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by Jocelyn on 03/08/2017.
 */

object PlaylistManager {


    fun findAll(): RXModelQueriableImpl<ZikoPlaylist> {
        return (select.from(ZikoPlaylist::class.java)
                .orderBy(OrderBy.fromProperty(ZikoPlaylist_Table.name).collate(Collate.NOCASE).ascending())
                .rx())
    }

    fun save() {

    }

    fun findOne(playlistId: Long): RXModelQueriableImpl<ZikoPlaylist> {
        return (select.from(ZikoPlaylist::class.java)
                .where(ZikoPlaylist_Table.id.eq(playlistId))
                .rx())
    }

    fun syncSpotify(): Observable<List<ZikoPlaylist>> {

        return SpotifyApiManager.INSTANCE.userPlaylists.flatMap({
            val zikoPlaylist = arrayListOf<ZikoPlaylist>()
            for (spo in it.items) {
                val zikoP = ZikoPlaylist.fromSpotifyPlaylist(spo)
                zikoP.save()
                zikoPlaylist.add(zikoP)
            }
            return@flatMap Observable.just(zikoPlaylist)
        })
    }

    fun syncSpotifyPlaylist(zikoSpotifyPlaylist: ZikoPlaylist): Observable<ZikoPlaylist> {
        return SpotifyApiManager.INSTANCE.getPlaylistTracks(zikoSpotifyPlaylist.spotifyId, 50, 0)
                .flatMap({
                    val tracks = arrayListOf<ZikoTrack>()
                    for (track in it.items) {
                        val track = ZikoTrack.from(track.track)
                        track.save()
                        tracks.add(track)
                    }
                    zikoSpotifyPlaylist.tracks = tracks
                    zikoSpotifyPlaylist.save()
                    return@flatMap Observable.just(zikoSpotifyPlaylist)
                })
    }
}
