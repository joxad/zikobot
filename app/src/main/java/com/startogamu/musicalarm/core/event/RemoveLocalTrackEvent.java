package com.startogamu.musicalarm.core.event;

import com.startogamu.musicalarm.module.alarm.object.LocalTrack;

import lombok.Data;

/**
 * Created by josh on 02/06/16.
 */
@Data
public class RemoveLocalTrackEvent {
    private final LocalTrack track;

}
