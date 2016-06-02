package com.startogamu.musicalarm.core.event;

import com.startogamu.musicalarm.module.spotify_api.object.SpotifyTrack;

import lombok.Data;

/**
 * Created by josh on 02/06/16.
 */
@Data
public class SelectTrackEvent {
    private final SpotifyTrack track;

}
