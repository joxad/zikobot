package com.startogamu.zikobot.module.component;


import com.startogamu.zikobot.module.spotify_api.SpotifyApiBaseComponent;
import com.startogamu.zikobot.module.spotify_api.SpotifyApiModule;
import com.startogamu.zikobot.module.spotify_auth.SpotifyAuthBaseComponent;
import com.startogamu.zikobot.module.spotify_auth.SpotifyAuthModule;
import com.startogamu.zikobot.viewmodel.activity.ActivityMainVM;
import com.startogamu.zikobot.viewmodel.fragment.alarm.FragmentAlarmsVM;
import com.startogamu.zikobot.viewmodel.activity.ActivitySettingsVM;
import com.startogamu.zikobot.viewmodel.activity.ActivityWakeUpVM;

import javax.inject.Singleton;

import dagger.Component;

/**
 * {@link SpotifyAuthComponent} use {@link SpotifyApiBaseComponent} pour l'injecter dans les vues
 * qui utiliseront les méthodes {@link SpotifyApiModule}
 */
@Singleton
@Component(modules = SpotifyAuthModule.class)
public interface SpotifyAuthComponent extends SpotifyAuthBaseComponent {

    void inject(ActivitySettingsVM activitySettingsVM);

    void inject(FragmentAlarmsVM fragmentAlarmsVM);

    void inject(ActivityWakeUpVM activityWakeUpVM);

    void inject(ActivityMainVM activityMainVM);

    //inject
}
