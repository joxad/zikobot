package com.joxad.zikobot.app.player.event;

import com.joxad.zikobot.app.localtracks.TrackVM;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 09/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventPlayTrack {
    private final TrackVM track;
}
