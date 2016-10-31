package com.startogamu.zikobot.core.event.player;

import com.startogamu.zikobot.localtracks.TrackVM;

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
