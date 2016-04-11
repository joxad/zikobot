package com.startogamu.musicalarm.module.component;

import com.startogamu.musicalarm.module.spotify_api.SpotifyApiBaseComponent;
import com.startogamu.musicalarm.module.spotify_api.SpotifyApiModule;
import com.startogamu.musicalarm.viewmodel.ActivitySettingsViewModel;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyConnectViewModel;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyPlaylistTracksViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * {@link SpotifyApiComponent} use {@link SpotifyApiBaseComponent} pour l'injecter dans les vues
 * qui utiliseront les méthodes {@link SpotifyApiModule}
 */
@Singleton
@Component(modules = SpotifyApiModule.class)
public interface SpotifyApiComponent extends SpotifyApiBaseComponent {
    void inject(SpotifyConnectViewModel spotifyConnectViewModel);

    void inject(SpotifyPlaylistTracksViewModel spotifyPlaylistTracksViewModel);

    void inject(ActivitySettingsViewModel activitySettingsViewModel);


    //inject
}
