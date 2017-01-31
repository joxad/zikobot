package com.joxad.zikobot.data.event.search;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 25/08/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventQueryChange {
    private final String query;
}
