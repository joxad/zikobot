package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoAlbum
import com.joxad.zikobot.data.db.model.ZikoArtist
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.rx2.language.RXModelQueriableImpl

/**
 * Created by Jocelyn on 03/08/2017.
 */

object AlbumManager {

    fun findAll(): RXModelQueriableImpl<ZikoAlbum> {
        return (select.from(ZikoAlbum::class.java)
                .rx())
    }
}
