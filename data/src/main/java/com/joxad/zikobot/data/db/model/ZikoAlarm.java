package com.joxad.zikobot.data.db.model;


import com.joxad.zikobot.data.db.ZikoDB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 08/03/16.
 */
@Table(database = ZikoDB.class)
public class ZikoAlarm extends BaseModel {

    @ForeignKey(stubbedRelationship = true)
    public ZikoPlaylist zikoPlaylist;

    @PrimaryKey(autoincrement = true)
    @Getter
    @Setter
    protected long id;
    @Column
    @Getter
    @Setter
    protected int hour;
    @Column
    @Getter
    @Setter
    protected int minute;
    @Column
    @Getter
    @Setter
    protected int active;

    @Column
    @Getter
    @Setter
    protected int repeated;

    @Column
    @Getter
    @Setter
    protected String days = "-0000000";

    @Column
    @Getter
    @Setter
    protected int randomTrack;

    @Column
    @Getter
    @Setter
    protected int volume;

    @Column
    @Getter
    @Setter
    protected long timeInMillis;

    /***
     * Default constructor =>  8 00 am
     */
    public ZikoAlarm() {
        hour = 8;
        minute = 0;
        active = 0;
        volume = -1;
    }


    public boolean isRandom() {
        return randomTrack == 1;
    }

    public boolean isDayActive(int day) {
        StringBuilder daysBuilder = new StringBuilder(days);
        char dayStatus = daysBuilder.charAt(day);
        return dayStatus == '1';
    }

    public void activeDay(int day, Boolean aBoolean) {
        replaceChar(aBoolean ? '1' : '0', day);
    }

    public void replaceChar(char c, int index) {
        StringBuilder daysBuilder = new StringBuilder(days);
        daysBuilder.setCharAt(index, c);
        days = daysBuilder.toString();
    }

}
