package com.startogamu.musicalarm.model;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 29/03/16.
 */
public class AlarmTrack extends RealmObject {

    public AlarmTrack() {
    }


    @Getter
    @Setter
    private String ref;

    @Getter
    @Setter
    private int type;


    public static class TYPE {
        public static final int SPOTIFY = 0;
        public static final int DEEZER = 1;
        public static final int LOCAL = 2;

    }
}
