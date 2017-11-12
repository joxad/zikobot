package com.joxad.zikobot.data.db.model

import com.joxad.zikobot.data.db.ZikoDB
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyArtist
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ConflictAction
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel


/**
 * Created by josh on 08/08/16.
 */
@Table(database = ZikoDB::class, insertConflict = ConflictAction.REPLACE)
class ZikoArtist : BaseModel() {

    @PrimaryKey(autoincrement = true)
    @Column
    var id: Int = 0
    @Column
    var localId: Long = 0
    @Column
    var spotifyId: String? = null
    @Column
    var soundcloudId: Int = 0
    @Column
    var name: String = ""
    @Column
    var image: String = ""

    lateinit var tracks: MutableList<ZikoTrack>

    companion object {

        fun local(localId: Long, name: String): ZikoArtist {
            val zikoArtist = ZikoArtist()
            zikoArtist.localId = localId
            zikoArtist.name = name
            return zikoArtist
        }


        fun spotify(spoId: String?, name: String): ZikoArtist {
            val zikoArtist = ZikoArtist()
            zikoArtist.spotifyId = spoId
            zikoArtist.name = name
            return zikoArtist
        }

        fun empty(): ZikoArtist {
            val zikoArtist = ZikoArtist()
            zikoArtist.name = "Loading"
            return zikoArtist
        }

        fun spotify(spoArtist: SpotifyArtist): ZikoArtist {
            val zikoArtist = ZikoArtist()
            zikoArtist.spotifyId = spoArtist.id
            zikoArtist.name = spoArtist.name!!
            return zikoArtist
        }
    }


}
