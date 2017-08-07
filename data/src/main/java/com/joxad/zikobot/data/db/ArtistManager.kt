package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoArtist
import com.joxad.zikobot.data.db.model.ZikoArtist_Table
import com.raizlabs.android.dbflow.annotation.Collate
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.rx2.language.RXModelQueriableImpl
import com.raizlabs.android.dbflow.sql.language.OrderBy

/**
 * Created by Jocelyn on 03/08/2017.
 */

object ArtistManager {

    fun findAll(): RXModelQueriableImpl<ZikoArtist> {
        return (select.from(ZikoArtist::class.java)
                .orderBy(OrderBy.fromProperty(ZikoArtist_Table.name).collate(Collate.NOCASE).ascending())
                .rx())
    }
}
