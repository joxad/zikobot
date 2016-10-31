package com.startogamu.zikobot.core.event.alarm;

import android.view.View;

import com.startogamu.zikobot.core.model.Alarm;

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
