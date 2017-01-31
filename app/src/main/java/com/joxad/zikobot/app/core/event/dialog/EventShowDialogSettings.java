package com.joxad.zikobot.app.core.event.dialog;

import com.joxad.zikobot.app.core.model.Track;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 20/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventShowDialogSettings {
    protected final Track model;
}
