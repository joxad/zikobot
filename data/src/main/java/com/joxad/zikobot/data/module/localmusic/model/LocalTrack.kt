package com.joxad.zikobot.data.module.localmusic.model

class LocalTrack(val id: Int, val title: String?, val trackNumber: Int, val year: Int, val duration: Long, val data: String?, val dateModified: Long, val albumId: Int, val albumName: String?, val artistId: Long, val artistName: String?) {


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