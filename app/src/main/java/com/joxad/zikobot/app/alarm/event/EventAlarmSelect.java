package com.joxad.zikobot.app.alarm.event;

import android.view.View;

import com.joxad.zikobot.data.model.Alarm;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 20/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventAlarmSelect {
    private final Alarm model;
    private final View view;
}
