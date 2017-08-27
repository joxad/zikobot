package com.joxad.zikobot.data.db

import com.joxad.zikobot.data.db.model.ZikoAlbum
import com.joxad.zikobot.data.db.model.ZikoAlbum_Table
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.joxad.zikobot.data.db.model.ZikoTrack_Table
import com.raizlabs.android.dbflow.annotation.Collate
import com.raizlabs.android.dbflow.kotlinextensions.and
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.rx2.language.RXModelQueriableImpl
import com.raizlabs.android.dbflow.sql.language.OrderBy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Jocelyn on 03/08/2017.
 */

object AlbumManager {

    fun findAll(): Single<MutableList<ZikoAlbum>> {
        return select.from(ZikoAlbum::class.java)
                .orderBy(OrderBy.fromProperty(ZikoAlbum_Table.name).collate(Collate.NOCASE).ascending())
                .rx()
                .queryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun findOne(id: Int): RXModelQueriableImpl<ZikoAlbum> {
        return (select.from(ZikoAlbum::class.java)
                .where(ZikoAlbum_Table.id.eq(id))
                .rx())
    }

    fun findTracks(artistId: Int, offset: Int): Single<MutableList<ZikoTrack>> {
        return select
                .from(ZikoTrack::class.java)
                .where(ZikoTrack_Table.zikoAlbum_id.eq(artistId))//.and(ZikoTrack_Table.zikoPlaylist_id.notEq(1)))
                .offset(offset * ZikoDB.PAGINATED_OFFSET)
                .limit(ZikoDB.PAGINATED_OFFSET)
                .rx()
                .queryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

}
