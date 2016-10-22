package com.startogamu.zikobot.core.event;

import com.startogamu.zikobot.localnetwork.ItemNetwork;

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
