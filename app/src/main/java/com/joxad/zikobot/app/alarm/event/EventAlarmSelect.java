package com.joxad.zikobot.app.alarm.event;

import android.view.View;

import com.joxad.zikobot.data.db.model.ZikoAlarm;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 20/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventAlarmSelect {
    private final ZikoAlarm model;
    private final View view;
}
