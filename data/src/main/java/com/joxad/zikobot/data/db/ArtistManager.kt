package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoArtist
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.rx2.language.RXModelQueriableImpl

/**
 * Created by Jocelyn on 03/08/2017.
 */

object ArtistManager {

    fun findAll(): RXModelQueriableImpl<ZikoArtist> {
        return (select.from(ZikoArtist::class.java)
                .rx())
    }
}
