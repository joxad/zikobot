package com.joxad.zikobot.app.alarm.event;

import com.joxad.zikobot.app.core.model.Alarm;

import lombok.Data;

/**
 * Created by Jocelyn on 20/01/2017.
 */
@Data
public class EventEditAlarm {
    private final Alarm alarm;
}
