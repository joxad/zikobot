package com.startogamu.zikobot.core.event.connect;

import com.startogamu.zikobot.module.spotify_api.model.SpotifyUser;

import lombok.Data;

/**
 * Created by josh on 10/06/16.
 */
@Data
public class EventSpotifyConnect {
    private final SpotifyUser spotifyUser;
}
