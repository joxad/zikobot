package com.joxad.zikobot.data.module.localmusic.manager;

import android.content.Context;

import com.joxad.zikobot.data.db.ZikoDB;
import com.joxad.zikobot.data.db.model.ZikoAlbum;
import com.joxad.zikobot.data.db.model.ZikoArtist;
import com.joxad.zikobot.data.db.model.ZikoArtist_Table;
import com.joxad.zikobot.data.db.model.ZikoTrack;
import com.joxad.zikobot.data.db.model.ZikoTrack_Table;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;

/**
 * Created by Josh on 06/04/2016.
 */

public enum LocalMusicManager {
    INSTANCE;

    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public void syncLocalData() {
        for (LocalTrack localTrack : TrackLoader.getAllTracks(context)) {
            createTrack(localTrack);
            createArtistIfNeed(localTrack.getArtistId(), localTrack.getArtistName());
            //     createAlbumIfNeed(localTrack.getAlbumId(), localTrack.getAlbumName(), localTrack.getArtistId(), localTrack.getArtistName());
        }
    }

    private void createAlbumIfNeed(int albumId, String albumName, int artistId, String artistName) {
        //if (!ZikoDB.exists(ZikoAlbum.class, ZikoAlbum))
        ZikoAlbum zikoAlbum = new ZikoAlbum(albumId, albumName);
        zikoAlbum.save();
    }

    private void createArtistIfNeed(long artistId, String artistName) {
        if (!ZikoDB.exists(ZikoArtist.class, ZikoArtist_Table.localId.eq(artistId))) {
            ZikoArtist zikoArtist = ZikoArtist.local(artistId, artistName);
            zikoArtist.save();
        }
    }

    private void createTrack(LocalTrack localTrack) {
        if (!ZikoDB.exists(ZikoTrack.class, ZikoTrack_Table.ref.eq(localTrack.getData()))) {
            ZikoTrack zikoTrack = ZikoTrack.local(localTrack);
            zikoTrack.save();
        }
    }
}
