package com.joxad.zikobot.app.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 19/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventShowMessage {
    private final String title;
    private final String string;
}
