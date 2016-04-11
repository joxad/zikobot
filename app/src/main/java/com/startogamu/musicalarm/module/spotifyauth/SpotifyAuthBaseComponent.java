package com.startogamu.musicalarm.module.spotifyauth;


import com.startogamu.musicalarm.module.spotifyauth.manager.SpotifyAuthManager;

/**
 * @author Fabien Baron
 * @company Phoceis
 * @email fbaron@phoceis.com
 * @date 11/04/2016
 */
public interface SpotifyAuthBaseComponent {
    SpotifyAuthManager spotifyAuthManager();
}
