package com.startogamu.musicalarm.module.spotify_auth;


import com.startogamu.musicalarm.module.spotify_auth.manager.SpotifyAuthManager;

/**
 * @author Fabien Baron
 * @company Phoceis
 * @email fbaron@phoceis.com
 * @date 11/04/2016
 */
public interface SpotifyAuthBaseComponent {
    SpotifyAuthManager manager();
}
