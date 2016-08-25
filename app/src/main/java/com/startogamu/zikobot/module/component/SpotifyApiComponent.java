package com.startogamu.zikobot.module.component;

import com.startogamu.zikobot.module.spotify_api.SpotifyApiBaseComponent;
import com.startogamu.zikobot.module.spotify_api.SpotifyApiModule;
import com.startogamu.zikobot.viewmodel.activity.ActivitySettingsVM;
import com.startogamu.zikobot.viewmodel.fragment.spotify.FragmentSpotifyTracksVM;

import javax.inject.Singleton;

import dagger.Component;

/**
 * {@link SpotifyApiComponent} use {@link SpotifyApiBaseComponent} pour l'injecter dans les vues
 * qui utiliseront les méthodes {@link SpotifyApiModule}
 */
@Singleton
@Component(modules = SpotifyApiModule.class)
public interface SpotifyApiComponent extends SpotifyApiBaseComponent {

    void inject(FragmentSpotifyTracksVM fragmentSpotifyTracksVM);

    void inject(ActivitySettingsVM activitySettingsVM);


    //inject
}
