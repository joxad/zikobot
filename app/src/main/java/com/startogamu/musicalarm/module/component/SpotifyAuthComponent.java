package com.startogamu.musicalarm.module.component;


import com.startogamu.musicalarm.module.spotify_api.SpotifyApiBaseComponent;
import com.startogamu.musicalarm.module.spotify_api.SpotifyApiModule;
import com.startogamu.musicalarm.module.spotify_auth.SpotifyAuthBaseComponent;
import com.startogamu.musicalarm.module.spotify_auth.SpotifyAuthModule;
import com.startogamu.musicalarm.viewmodel.ActivityAlarmsVM;
import com.startogamu.musicalarm.viewmodel.ActivitySettingsVM;
import com.startogamu.musicalarm.viewmodel.activity.ActivityWakeUpVM;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyConnectViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * {@link SpotifyAuthComponent} use {@link SpotifyApiBaseComponent} pour l'injecter dans les vues
 * qui utiliseront les méthodes {@link SpotifyApiModule}
 */
@Singleton
@Component(modules = SpotifyAuthModule.class)
public interface SpotifyAuthComponent extends SpotifyAuthBaseComponent {
    void inject(SpotifyConnectViewModel spotifyConnectViewModel);

    void inject(ActivitySettingsVM activitySettingsVM);

    void inject(ActivityAlarmsVM activityAlarmsVM);

    void inject(ActivityWakeUpVM activityWakeUpVM);

    //inject
}
