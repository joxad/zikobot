package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoTrack
import com.joxad.zikobot.data.db.model.ZikoTrack_Table
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.rx2.language.RXModelQueriableImpl

/**
 * Created by Jocelyn on 23/10/2017.
 */
object TrackManager {


    fun findOne(id: Long): RXModelQueriableImpl<ZikoTrack> {
        return (select.from(ZikoTrack::class.java)
                .where(ZikoTrack_Table.id.eq(id))
                .rx())
    }
}