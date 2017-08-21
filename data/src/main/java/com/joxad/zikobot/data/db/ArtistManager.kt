package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoArtist
import com.joxad.zikobot.data.db.model.ZikoArtist_Table
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.joxad.zikobot.data.db.model.ZikoTrack_Table
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
                .orderBy(OrderBy.fromProperty(ZikoArtist_Table.name).collate(Collate.NOCASE)
                        .ascending())
                .rx())
    }

    fun findAllPaginated(offset: Int): RXModelQueriableImpl<ZikoArtist> {
        return (select.from(ZikoArtist::class.java)
                .offset(offset * ZikoDB.PAGINATED_OFFSET)
                .limit(ZikoDB.PAGINATED_OFFSET)
                .orderBy(OrderBy.fromProperty(ZikoArtist_Table.name).collate(Collate.NOCASE)
                        .ascending())
                .rx())
    }


    fun findOne(artistId: Int): RXModelQueriableImpl<ZikoArtist> {
        return (select.from(ZikoArtist::class.java)
                .where(ZikoArtist_Table.id.eq(artistId))
                .rx())
    }

    fun findTracks(artistId: Int, offset: Int): RXModelQueriableImpl<ZikoTrack> {
        return select
                .from(ZikoTrack::class.java)
                .where(ZikoTrack_Table.zikoArtist_id.eq(artistId))
                .offset(offset * ZikoDB.PAGINATED_OFFSET)
                .limit(ZikoDB.PAGINATED_OFFSET)
                .rx()

    }

}
