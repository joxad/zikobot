package com.startogamu.zikobot.core.event.dialog;

import com.startogamu.zikobot.module.zikobot.model.Track;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 20/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventShowDialogAlarm {
    protected final Track model;
}
