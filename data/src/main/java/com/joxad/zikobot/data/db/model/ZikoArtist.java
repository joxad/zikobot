package com.joxad.zikobot.data.db.model;

import com.joxad.zikobot.data.db.ZikoDB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;


/**
 * Created by josh on 08/08/16.
 */
@Table(database = ZikoDB.class, insertConflict = ConflictAction.REPLACE)
public class ZikoArtist extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    protected int id;
    @Column
    protected long localId;
    @Column
    protected String spotifyId;
    @Column
    protected int soundcloudId;
    @Column
    protected String name;
    @Column
    protected String image;

    public ZikoArtist() {
    }

    public static ZikoArtist local(long localId, String name) {
        ZikoArtist zikoArtist = new ZikoArtist();
        zikoArtist.localId = localId;
        zikoArtist.name = name;
        return zikoArtist;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

}
