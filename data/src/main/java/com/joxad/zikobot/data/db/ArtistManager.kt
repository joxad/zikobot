package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoArtist
import com.joxad.zikobot.data.db.model.ZikoArtist_Table
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.joxad.zikobot.data.db.model.ZikoTrack_Table
import com.raizlabs.android.dbflow.annotation.Collate
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.sql.language.OrderBy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Jocelyn on 03/08/2017.
 */

object ArtistManager {

    fun findAll(): Single<MutableList<ZikoArtist>> {
        return select.from(ZikoArtist::class.java)
                .orderBy(OrderBy.fromProperty(ZikoArtist_Table.name).collate(Collate.NOCASE)
                        .ascending())
                .rx()
                .queryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun findAllPaginated(offset: Int): Single<MutableList<ZikoArtist>> {
        return select.from(ZikoArtist::class.java)
                .offset(offset * ZikoDB.PAGINATED_OFFSET)
                .limit(ZikoDB.PAGINATED_OFFSET)
                .orderBy(OrderBy.fromProperty(ZikoArtist_Table.name).collate(Collate.NOCASE)
                        .ascending())
                .rx()
                .queryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun findOne(artistId: Int): Single<ZikoArtist> {
        return select.from(ZikoArtist::class.java)
                .where(ZikoArtist_Table.id.eq(artistId))
                .rx()
                .querySingle()
    }

    fun findTracks(artistId: Int, offset: Int): Single<MutableList<ZikoTrack>> {
        return select
                .from(ZikoTrack::class.java)
                .where(ZikoTrack_Table.zikoArtist_id.eq(artistId))//.and(ZikoTrack_Table.zikoPlaylist_id.notEq(1)))
                .offset(offset * ZikoDB.PAGINATED_OFFSET)
                .limit(ZikoDB.PAGINATED_OFFSET)
                .rx()
                .queryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun findAllTracks(): Single<MutableList<ZikoTrack>> {
        return select
                .from(ZikoTrack::class.java)
                .rx()
                .queryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

}
