package com.startogamu.zikobot.module.component;

import com.startogamu.zikobot.module.soundcloud.SoundCloudApiBaseComponent;
import com.startogamu.zikobot.module.soundcloud.SoundCloudApiModule;
import com.startogamu.zikobot.viewmodel.activity.ActivitySettingsVM;
import com.startogamu.zikobot.viewmodel.fragment.soundcloud.FragmentSoundCloudPlaylistsVM;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by josh on 15/06/16.
 */
@Singleton
@Component(modules = SoundCloudApiModule.class)
public interface SoundCloudApiComponent extends SoundCloudApiBaseComponent {
    void inject(ActivitySettingsVM activitySettingsVM);

    void inject(FragmentSoundCloudPlaylistsVM fragmentSoundCloudPlaylistsVM);
}
