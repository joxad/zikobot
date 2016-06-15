package com.startogamu.zikobot.core.event.navigation_manager;

import lombok.Data;

/**
 * This event is registered :
 * {@link com.startogamu.zikobot.core.fragmentmanager.NavigationManager}
 * and called from:
 * {@link com.startogamu.zikobot.viewmodel.fragment.spotify.FragmentSpotifyPlaylistsVM}
 * {@link com.startogamu.zikobot.view.fragment.local.FragmentLocalArtists}
 */
@Data
public class EventCollapseToolbar {
    private final String name;
    private final String imageUrl;
}
