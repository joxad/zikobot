package com.joxad.zikobot.data.module.localmusic.manager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

import com.joxad.zikobot.data.db.model.ZikoAlbum
import com.joxad.zikobot.data.db.model.ZikoAlbum_Table
import com.joxad.zikobot.data.db.model.ZikoArtist
import com.joxad.zikobot.data.db.model.ZikoArtist_Table
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.joxad.zikobot.data.db.model.ZikoTrack_Table
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyResultTracks
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyTrack
import com.raizlabs.android.dbflow.sql.language.Select

import java.io.File

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject


/**
 * Created by Josh on 06/04/2016.
 */

class LocalMusicManager(private val context: Context) {
    private val synchroDone: PublishSubject<Boolean>
    private val cr: ContentResolver

    init {
        synchroDone = PublishSubject.create()
        cr = context.contentResolver
    }

    fun observeSynchro(): Observable<Boolean> {
        return Observable.fromCallable {
            try {
                syncLocalData()
            } catch (e: Exception) {
                Log.d(LocalMusicManager::class.java.simpleName, e.localizedMessage)
            }

            synchroDone.onNext(true)
            true
        }
    }


    fun syncLocalData() {
        try {
            for (localTrack in TrackLoader.getAllTracks(context)) {
                val zikoArtist = createArtistIfNeed(localTrack.artistId, localTrack.artistName)
                val zikoAlbum = createAlbumIfNeed(localTrack.albumId.toLong(), localTrack.albumName, zikoArtist)
                createTrack(localTrack, zikoArtist, zikoAlbum)
            }
        } catch (e: Exception) {
            Log.e(LocalMusicManager::class.java.simpleName, e.localizedMessage)
        }

    }

    private fun createArtistIfNeed(artistId: Long, artistName: String?): ZikoArtist {
        var zikoArtist = Select().from(ZikoArtist::class.java).where(ZikoArtist_Table.localId.eq(artistId)).querySingle()
        if (zikoArtist == null) {
            zikoArtist = ZikoArtist.local(artistId, artistName!!)
            zikoArtist.favorite = true
            zikoArtist.save()
        }
        return zikoArtist
    }

    private fun createAlbumIfNeed(albumId: Long, albumName: String?, zikoArtist: ZikoArtist): ZikoAlbum {
        var zikoAlbum = Select().from(ZikoAlbum::class.java)
                .where(ZikoAlbum_Table.localId.eq(albumId))
                .or(ZikoAlbum_Table.name.like(albumName!!))
                .querySingle()
        if (zikoAlbum == null) {
            zikoAlbum = ZikoAlbum.local(albumId, albumName, zikoArtist)
            val albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId)
            if (isUriBitmapValid(albumArtUri))
                zikoAlbum.image = albumArtUri.toString()
            zikoAlbum.favorite = true
            zikoAlbum.save()
        }
        return zikoAlbum
    }


    private fun isUriBitmapValid(uri: Uri): Boolean {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cur = cr.query(uri, projection, null, null, null)
        var b = false
        if (cur != null) {
            cur.moveToFirst()
            try {

                val filePath = cur.getString(0)
                if (filePath == null || filePath.isEmpty()) {
                    b = false
                } else
                    b = File(filePath).exists()
            } catch (e: CursorIndexOutOfBoundsException) {
                b = false
            }


        } else {
            b = false
        }
        return b
    }

    private fun createTrack(localTrack: LocalTrack, artist: ZikoArtist, album: ZikoAlbum): ZikoTrack {
        var zikoTrack = Select().from(ZikoTrack::class.java).where(ZikoTrack_Table.localId.eq(localTrack.id)).querySingle()
        if (zikoTrack == null) {
            zikoTrack = ZikoTrack.local(localTrack, artist, album)
            zikoTrack!!.save()
        }
        return zikoTrack
    }

    fun synchroDoneObservable(): Single<Boolean> {
        return synchroDone.lastOrError()
    }

    companion object {

        val sArtworkUri = Uri
                .parse("content://media/external/audio/albumart")
    }
}
