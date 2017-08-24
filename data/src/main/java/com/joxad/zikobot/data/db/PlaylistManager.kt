package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.*
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyAlbum
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyArtist
import com.raizlabs.android.dbflow.annotation.Collate
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.rx2.language.RXModelQueriableImpl
import com.raizlabs.android.dbflow.sql.language.OrderBy
import com.raizlabs.android.dbflow.sql.language.Select
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by Jocelyn on 03/08/2017.
 */

enum class PlaylistManager {
    INSTANCE;

    lateinit var refreshSubject: BehaviorSubject<Boolean>

    fun init() {
        refreshSubject = BehaviorSubject.create()
        if (select.from(ZikoPlaylist::class.java).count() == 0L)
            ZikoPlaylist("current", 0, "", ArrayList()).save()
    }

    fun findAll(): RXModelQueriableImpl<ZikoPlaylist> {
        return (select.from(ZikoPlaylist::class.java)
                .where(ZikoPlaylist_Table.id.notEq(1))
                .orderBy(OrderBy.fromProperty(ZikoPlaylist_Table.name).collate(Collate.NOCASE).ascending())
                .rx())
    }

    fun save() {

    }

    fun findPlayingPlaylist(): ZikoPlaylist? {
        return select.from(ZikoPlaylist::class.java)
                .where(ZikoPlaylist_Table.id.eq(1)).querySingle()
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
                    for (track in it.items) {
                        val zikoArtist = createArtistIfNeed(track.track?.artists?.get(0)!!)
                        val zikoAlbum = createAlbumIfNeed(track.track?.album!!, zikoArtist!!)
                        val zikoTrack = ZikoTrack.spotify(track.track, zikoArtist, zikoAlbum, zikoSpotifyPlaylist)
                        zikoTrack.save()
                    }
                    zikoSpotifyPlaylist.save()
                    refreshSubject.onNext(true)
                    return@flatMap Observable.just(zikoSpotifyPlaylist)
                })
    }


    private fun createArtistIfNeed(artist: SpotifyArtist): ZikoArtist? {
        var zikoArtist = Select().from(ZikoArtist::class.java).where(ZikoArtist_Table.spotifyId.eq(artist.id)).querySingle()
        if (zikoArtist == null) {
            zikoArtist = ZikoArtist.spotify(artist.id, artist.name!!)
            zikoArtist.save()
        }
        return zikoArtist
    }


    private fun createAlbumIfNeed(album: SpotifyAlbum, zikoArtist: ZikoArtist): ZikoAlbum {
        var zikoAlbum = Select().from(ZikoAlbum::class.java)
                .where(ZikoAlbum_Table.spotifyId.eq(album.id))
                .or(ZikoAlbum_Table.name.like(album.name!!))
                .querySingle()
        if (zikoAlbum == null) {
            zikoAlbum = ZikoAlbum.spotify(album.id!!, album.name!!, zikoArtist, album.images.get(0).url!!)
            zikoAlbum.save()
        }
        return zikoAlbum
    }

    fun delete(playlist: ZikoPlaylist) {
        playlist.delete()
    }

    fun findTracks(playlistId: Long, offset: Int): RXModelQueriableImpl<ZikoTrack> {
        return select
                .from(ZikoTrack::class.java)
                .where(ZikoTrack_Table.zikoPlaylist_id.eq(playlistId))
                .offset(offset * ZikoDB.PAGINATED_OFFSET)
                .limit(ZikoDB.PAGINATED_OFFSET)
                .rx()
    }


}
