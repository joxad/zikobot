package com.joxad.zikobot.app.core.event.alarm;

import android.view.View;

import com.joxad.zikobot.app.core.model.Alarm;

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
