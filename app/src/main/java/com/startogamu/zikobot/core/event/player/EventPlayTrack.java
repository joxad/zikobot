package com.startogamu.zikobot.core.event.player;

import com.startogamu.zikobot.module.alarm.model.Track;

import lombok.Data;

/**
 * Created by josh on 09/06/16.
 */
@Data
public class EventPlayTrack {
    private final Track track;
}
