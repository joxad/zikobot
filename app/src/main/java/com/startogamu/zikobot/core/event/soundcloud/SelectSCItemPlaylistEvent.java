package com.startogamu.zikobot.core.event.soundcloud;

import com.startogamu.zikobot.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.module.spotify_api.model.Item;

import lombok.Data;

/**
 * Created by josh on 02/06/16.
 */
@Data
public class SelectSCItemPlaylistEvent {
    private final SoundCloudPlaylist item;
}
