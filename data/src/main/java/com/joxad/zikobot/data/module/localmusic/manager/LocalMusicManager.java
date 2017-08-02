package com.joxad.zikobot.data.module.localmusic.manager;

import android.content.Context;

import com.joxad.zikobot.data.db.model.ZikoAlbum;
import com.joxad.zikobot.data.db.model.ZikoAlbum_Table;
import com.joxad.zikobot.data.db.model.ZikoArtist;
import com.joxad.zikobot.data.db.model.ZikoArtist_Table;
import com.joxad.zikobot.data.db.model.ZikoTrack;
import com.joxad.zikobot.data.db.model.ZikoTrack_Table;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;
import com.raizlabs.android.dbflow.sql.language.Select;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;


/**
 * Created by Josh on 06/04/2016.
 */

public enum LocalMusicManager {
    INSTANCE;

    private Context context;

    public BehaviorSubject<Boolean> synchroDone;

    public void init(Context context) {
        this.context = context;
        synchroDone = BehaviorSubject.create();

    }

    public Observable<Boolean> observeSynchro() {
        return Observable.fromCallable(() -> {
            syncLocalData();
            synchroDone.onNext(true);
            return true;
        });
    }


    public void syncLocalData() {
        for (LocalTrack localTrack : TrackLoader.getAllTracks(context)) {
            ZikoArtist zikoArtist = createArtistIfNeed(localTrack.getArtistId(), localTrack.getArtistName());
            ZikoAlbum zikoAlbum = createAlbumIfNeed(localTrack.getAlbumId(), localTrack.getAlbumName(), zikoArtist);
            createTrack(localTrack, zikoArtist, zikoAlbum);
        }
    }


    private ZikoArtist createArtistIfNeed(long artistId, String artistName) {
        ZikoArtist zikoArtist = new Select().from(ZikoArtist.class).where(ZikoArtist_Table.localId.eq(artistId)).querySingle();
        if (zikoArtist == null) {
            zikoArtist = ZikoArtist.local(artistId, artistName);
            zikoArtist.save();
        }
        return zikoArtist;
    }

    private ZikoAlbum createAlbumIfNeed(long albumId, String albumName, ZikoArtist zikoArtist) {
        ZikoAlbum zikoAlbum = new Select().from(ZikoAlbum.class)
                .where(ZikoAlbum_Table.localId.eq(albumId))
                .or(ZikoAlbum_Table.name.like(albumName))
                .querySingle();
        if (zikoAlbum == null) {
            zikoAlbum = ZikoAlbum.Companion.local(albumId, albumName, zikoArtist);
            zikoAlbum.save();
        }
        return zikoAlbum;
    }


    private ZikoTrack createTrack(LocalTrack localTrack, ZikoArtist artist, ZikoAlbum album) {
        ZikoTrack zikoTrack = new Select().from(ZikoTrack.class).where(ZikoTrack_Table.localId.eq(localTrack.getId())).querySingle();
        if (zikoTrack == null) {
            zikoTrack = ZikoTrack.local(localTrack, artist, album);
            zikoTrack.save();
        }
        return zikoTrack;
    }
}
