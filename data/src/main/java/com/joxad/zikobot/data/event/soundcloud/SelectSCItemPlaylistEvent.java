package com.joxad.zikobot.data.event.soundcloud;

import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 02/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SelectSCItemPlaylistEvent {
    private final SoundCloudPlaylist item;
}
