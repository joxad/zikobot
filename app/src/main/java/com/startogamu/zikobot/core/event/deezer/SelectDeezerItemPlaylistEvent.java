package com.startogamu.zikobot.core.event.deezer;


import com.deezer.sdk.model.Playlist;

import lombok.Data;

/**
 * Created by josh on 02/06/16.
 */
@Data
public class SelectDeezerItemPlaylistEvent {
    private final Playlist item;
}
