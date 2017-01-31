package com.joxad.zikobot.app.core.event;

import com.joxad.zikobot.app.localnetwork.ItemNetwork;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 29/08/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventSelectItemNetwork {
    private final ItemNetwork model;
}
