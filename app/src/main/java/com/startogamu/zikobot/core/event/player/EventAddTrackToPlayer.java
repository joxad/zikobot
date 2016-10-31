package com.startogamu.zikobot.core.event.player;

import android.databinding.ObservableArrayList;

import com.startogamu.zikobot.localtracks.TrackVM;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 12/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventAddTrackToPlayer {
    private final ObservableArrayList<TrackVM> items;
}
