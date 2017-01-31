package com.joxad.zikobot.data.event;

import com.joxad.zikobot.data.module.spotify_api.model.Item;

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
