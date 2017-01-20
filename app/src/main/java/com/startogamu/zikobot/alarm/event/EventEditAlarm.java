package com.startogamu.zikobot.alarm.event;

import com.startogamu.zikobot.core.model.Alarm;

import lombok.Data;

/**
 * Created by Jocelyn on 20/01/2017.
 */
@Data
public class EventEditAlarm {
    private final Alarm alarm;
}
