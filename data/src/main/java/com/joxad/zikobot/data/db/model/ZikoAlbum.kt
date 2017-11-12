package com.joxad.zikobot.data.db.model

import com.joxad.zikobot.data.db.ZikoDB
import com.joxad.zikobot.data.module.spotify_api.model.Albums
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyAlbum
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.structure.BaseModel

/**
 * Created by josh on 08/08/16.
 */
@Table(database = ZikoDB::class, insertConflict = ConflictAction.REPLACE)
class ZikoAlbum : BaseModel() {
    @Column
    @PrimaryKey(autoincrement = true)
    var id: Int = 0
    @Column
    var localId: Long = 0
    @Column
    var spotifyId: String? = null
    @Column
    var name: String = ""
    @Column
    public var image: String = ""
    @Column
    @ForeignKey(stubbedRelationship = false)
    var artist: ZikoArtist? = null

    companion object {

        fun local(localId: Long, name: String, artist: ZikoArtist): ZikoAlbum {
            val zikoAlbum = ZikoAlbum()
            zikoAlbum.localId = localId
            zikoAlbum.name = name
            zikoAlbum.artist = artist

            return zikoAlbum
        }

        fun spotify(spotifyId: String, name: String, artist: ZikoArtist, image:String): ZikoAlbum {
            val zikoAlbum = ZikoAlbum()
            zikoAlbum.spotifyId = spotifyId
            zikoAlbum.name = name
            zikoAlbum.artist = artist
            zikoAlbum.image = image
            return zikoAlbum
        }


        fun spotify(album: SpotifyAlbum): ZikoAlbum {
            val zikoAlbum = ZikoAlbum()
            zikoAlbum.spotifyId = album.id
            zikoAlbum.name = album.name!!
            zikoAlbum.artist = ZikoArtist.spotify(album.artists[0])
            zikoAlbum.image = album.images[0].url!!
            return zikoAlbum
        }
    }

}
