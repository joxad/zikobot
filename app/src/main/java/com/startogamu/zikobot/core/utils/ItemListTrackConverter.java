package com.startogamu.zikobot.core.utils;

import android.os.Parcel;

import com.startogamu.zikobot.module.alarm.model.AlarmTrack;

import org.parceler.Parcels;
import org.parceler.converter.ArrayListParcelConverter;

public class ItemListTrackConverter extends ArrayListParcelConverter<AlarmTrack> {
    @Override
    public void itemToParcel(AlarmTrack item, Parcel parcel) {
        parcel.writeParcelable(Parcels.wrap(item), 0);
    }

    @Override
    public AlarmTrack itemFromParcel(Parcel parcel) {
        return Parcels.unwrap(parcel.readParcelable(AlarmTrack.class.getClassLoader()));
    }
}