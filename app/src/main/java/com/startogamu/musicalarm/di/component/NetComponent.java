package com.startogamu.musicalarm.di.component;


import com.startogamu.musicalarm.di.module.AppModule;
import com.startogamu.musicalarm.core.service.AlarmService;
import com.startogamu.musicalarm.di.module.spotify_api.SpotifyAPINetworkModule;
import com.startogamu.musicalarm.di.module.spotify_api.SpotifyAPIServiceModule;
import com.startogamu.musicalarm.di.module.spotify_auth.SpotifyAuthNetworkModule;
import com.startogamu.musicalarm.di.module.spotify_auth.SpotifyAuthServiceModule;
import com.startogamu.musicalarm.viewmodel.ActivityAlarmViewModel;
import com.startogamu.musicalarm.viewmodel.ActivityAlarmsViewModel;
import com.startogamu.musicalarm.viewmodel.ActivitySettingsViewModel;
import com.startogamu.musicalarm.viewmodel.fragment.AlarmTracksViewModel;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyConnectViewModel;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyMusicViewModel;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyPlaylistTracksViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by josh on 24/03/16.
 */
@Singleton
@Component(modules = {AppModule.class,
        SpotifyAuthNetworkModule.class,
        SpotifyAuthServiceModule.class,
        SpotifyAPINetworkModule.class,
        SpotifyAPIServiceModule.class})
public interface NetComponent {

    void inject(ActivityAlarmsViewModel activityAlarmsViewModel);

    void inject(ActivityAlarmViewModel activityAlarmViewModel);

    void inject(ActivitySettingsViewModel activityAlarmViewModel);

    void inject(SpotifyMusicViewModel spotifyMusicViewModel);

    void inject(SpotifyConnectViewModel spotifyConnectViewModel);

    void inject(AlarmService alarmService);

    void inject(SpotifyPlaylistTracksViewModel spotifyPlaylistTracksViewModel);

    void inject(AlarmTracksViewModel alarmTracksViewModel);
}
