package com.startogamu.zikobot.core.event.player;

import com.startogamu.zikobot.module.zikobot.model.Track;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by manon on 04/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class TrackChangeEvent {
    private final Track track;
}
