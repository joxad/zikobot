package com.startogamu.zikobot.core.event;

import com.startogamu.zikobot.module.alarm.model.AlarmTrack;

import lombok.Data;

/**
 * Created by manon on 04/06/16.
 */
@Data
public class TrackChangeEvent {
    private final AlarmTrack track;
}
