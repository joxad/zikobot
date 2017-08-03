package com.joxad.zikobot.data.db.model

import android.provider.MediaStore
import com.joxad.zikobot.data.db.ZikoDB
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
    var spotifyId: Int = 0
    @Column
    lateinit var name: String
    @Column
    lateinit var image: String
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
    }

}
