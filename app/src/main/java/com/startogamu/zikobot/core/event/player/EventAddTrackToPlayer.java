package com.startogamu.zikobot.core.event.player;

import android.databinding.ObservableArrayList;

import com.startogamu.zikobot.viewmodel.base.TrackVM;

import lombok.Data;

/**
 * Created by josh on 12/06/16.
 */
@Data
public class EventAddTrackToPlayer {
    private final ObservableArrayList<TrackVM> items;
}
