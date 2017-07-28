package com.joxad.zikobot.data.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by josh on 08/08/16.
 */
public class ZikoAlbum extends BaseModel {
    @PrimaryKey(autoincrement = true)
    protected int id;
    @Column
    protected int localId;
    @Column
    protected int spotifyId;
    @Column
    protected String name;
    @Column
    protected String image;

    public ZikoAlbum() {
    }

    public ZikoAlbum(int albumId, String albumName) {
        this.localId = albumId;
        this.name = albumName;
    }


}
