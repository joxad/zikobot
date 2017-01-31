package com.joxad.zikobot.app.deezer.event;


import com.deezer.sdk.model.Playlist;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 02/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SelectDeezerItemPlaylistEvent {
    private final Playlist item;
}
