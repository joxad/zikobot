package com.startogamu.musicalarm.core.event;

import com.startogamu.musicalarm.module.alarm.model.AlarmTrack;

import lombok.Data;

/**
 * Created by manon on 04/06/16.
 */
@Data
public class TrackChangeEvent {
    private final AlarmTrack track;
}
