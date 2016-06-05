package com.startogamu.zikobot.core.event;

import com.startogamu.zikobot.module.spotify_api.model.Item;

import lombok.Data;

/**
 * Created by josh on 02/06/16.
 */
@Data
public class SelectItemPlaylistEvent {
    private final Item item;
}
