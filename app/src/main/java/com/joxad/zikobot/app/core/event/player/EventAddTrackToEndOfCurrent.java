package com.joxad.zikobot.app.core.event.player;

import com.joxad.zikobot.app.localtracks.TrackVM;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 27/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventAddTrackToEndOfCurrent {
    private final TrackVM trackVM;
}
