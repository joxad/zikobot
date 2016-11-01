package com.startogamu.zikobot.core.event;

import com.startogamu.zikobot.core.module.spotify_api.model.Item;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 02/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SelectItemPlaylistEvent {
    private final Item item;
}
