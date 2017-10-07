package com.joxad.zikobot.data.db.model

import com.joxad.zikobot.data.db.ZikoDB
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist
import com.joxad.zikobot.data.module.spotify_api.model.Item
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel
import java.util.*


/**
 * Created by josh on 19/06/16.
 */
@Table(database = ZikoDB::class)
class ZikoPlaylist : BaseModel {
    @PrimaryKey(autoincrement = true)
    var id: Long = 0
    @Column
    var name: String? = ""
    @Column
    var nbTracks: Int? = 0
    @Column
    var imageUrl: String? = ""
    @Column
    var spotifyId: String? = null
    @Column
    var soundcloudId: Int? = 0
    @ForeignKey(stubbedRelationship = true)
    var zikoAlarm: ZikoAlarm? = null

    lateinit var tracks: List<ZikoTrack>

    constructor() {}

    constructor(name: String?) {
        this.name = name
        this.tracks = arrayListOf()
    }


    constructor(name: String?, nbTracks: Int?, imageUrl: String?, tracks: ArrayList<ZikoTrack>) {
        this.name = name
        this.nbTracks = nbTracks
        this.imageUrl = imageUrl
        this.tracks = tracks
    }


    /****
     * @return
     */
    @OneToMany(methods = arrayOf(OneToMany.Method.SAVE, OneToMany.Method.DELETE), variableName = "tracks")
    fun getForeignTracks(): List<ZikoTrack> {
        tracks = SQLite.select()
                .from(ZikoTrack::class.java)
                .where(ZikoTrack_Table.zikoPlaylist_id.eq(id))
                .queryList()

        return tracks
    }


    companion object {

        fun fromSpotifyPlaylist(item: Item): ZikoPlaylist {

            val tracks = ArrayList<ZikoTrack>()
            val urlImage = if (item.images.isNotEmpty()) item.images[0].url else ""
            val zikoPlaylist = ZikoPlaylist(item.name, item.tracks?.total, urlImage, tracks)
            zikoPlaylist.spotifyId = item.id
            return zikoPlaylist
        }


        fun fromSoundCloudPlaylist(soundCloudPlaylist: SoundCloudPlaylist): ZikoPlaylist {
            val tracks = ArrayList<ZikoTrack>()
            for (soundCloudTrack in soundCloudPlaylist.soundCloudTracks) {
                tracks.add(ZikoTrack.from(soundCloudTrack))
            }
            val zikoPlaylist = ZikoPlaylist(soundCloudPlaylist.title, soundCloudPlaylist.trackCount!!, soundCloudPlaylist.artworkUrl, tracks)
            zikoPlaylist.soundcloudId = soundCloudPlaylist.id!!
            return zikoPlaylist
        }
    }


}
