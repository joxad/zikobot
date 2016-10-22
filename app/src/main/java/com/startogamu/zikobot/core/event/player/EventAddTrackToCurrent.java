package com.startogamu.zikobot.core.event.player;

import com.startogamu.zikobot.viewmodel.base.TrackVM;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 27/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventAddTrackToCurrent {
    private final TrackVM trackVM;
}
