package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoAlbum
import com.joxad.zikobot.data.db.model.ZikoAlbum_Table
import com.joxad.zikobot.data.db.model.ZikoArtist
import com.raizlabs.android.dbflow.annotation.Collate
import com.raizlabs.android.dbflow.kotlinextensions.property
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.rx2.language.RXModelQueriableImpl
import com.raizlabs.android.dbflow.sql.language.OrderBy

/**
 * Created by Jocelyn on 03/08/2017.
 */

object AlbumManager {

    fun findAll(): RXModelQueriableImpl<ZikoAlbum> {
        return (select.from(ZikoAlbum::class.java)
                .orderBy(OrderBy.fromProperty(ZikoAlbum_Table.name).collate(Collate.NOCASE).ascending())
                .rx())
    }
}
