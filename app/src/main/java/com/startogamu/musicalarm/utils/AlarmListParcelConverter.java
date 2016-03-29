package com.startogamu.musicalarm.utils;

import com.startogamu.musicalarm.model.AlarmTrack;

import org.parceler.Parcels;

// Specific class for a RealmList<Alarm> field
public class AlarmListParcelConverter extends RealmListParcelConverter<AlarmTrack> {

    @Override
    public void itemToParcel(AlarmTrack input, android.os.Parcel parcel) {
        parcel.writeParcelable(Parcels.wrap(input), 0);
    }

    @Override
    public AlarmTrack itemFromParcel(android.os.Parcel parcel) {
        return Parcels.unwrap(parcel.readParcelable(AlarmTrack.class.getClassLoader()));
    }
}
