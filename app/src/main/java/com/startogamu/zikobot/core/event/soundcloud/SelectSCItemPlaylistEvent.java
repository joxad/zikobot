package com.startogamu.zikobot.core.event.soundcloud;

import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.core.module.spotify_api.model.Item;

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
