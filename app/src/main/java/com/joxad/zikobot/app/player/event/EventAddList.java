package com.joxad.zikobot.app.player.event;

import android.databinding.ObservableArrayList;

import com.joxad.zikobot.app.localtracks.TrackVM;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 12/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventAddList {
    private final ObservableArrayList<TrackVM> items;
}
