package com.joxad.zikobot.data.module.localmusic.model

class LocalTrack(val id: Int, val title: String?, val trackNumber: Int, val year: Int, val duration: Long, val data: String?, val dateModified: Long, val albumId: Int, val albumName: String?, val artistId: Int, val artistName: String?) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null) return false

        val song = o as LocalTrack?

        if (id != song!!.id) return false
        if (trackNumber != song.trackNumber) return false
        if (year != song.year) return false
        if (duration != song.duration) return false
        if (dateModified != song.dateModified) return false
        if (albumId != song.albumId) return false
        if (artistId != song.artistId) return false
        if (if (title != null) title != song.title else song.title != null) return false
        if (if (data != null) data != song.data else song.data != null) return false
        if (if (albumName != null) albumName != song.albumName else song.albumName != null)
            return false
        return if (artistName != null) artistName == song.artistName else song.artistName == null

    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + trackNumber
        result = 31 * result + year
        result = 31 * result + (duration xor duration.ushr(32)).toInt()
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + (dateModified xor dateModified.ushr(32)).toInt()
        result = 31 * result + albumId
        result = 31 * result + (albumName?.hashCode() ?: 0)
        result = 31 * result + artistId
        result = 31 * result + (artistName?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", trackNumber=" + trackNumber +
                ", year=" + year +
                ", duration=" + duration +
                ", data='" + data + '\'' +
                ", dateModified=" + dateModified +
                ", albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                '}'
    }

    companion object {
        val EMPTY = LocalTrack(-1, "", -1, -1, -1, "", -1, -1, "", -1, "")
    }


}