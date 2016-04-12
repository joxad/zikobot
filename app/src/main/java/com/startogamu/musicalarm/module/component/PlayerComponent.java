package com.startogamu.musicalarm.module.component;

import com.startogamu.musicalarm.module.music.PlayerBaseComponent;
import com.startogamu.musicalarm.module.music.PlayerModule;
import com.startogamu.musicalarm.viewmodel.fragment.AlarmTracksViewModel;
import com.startogamu.musicalarm.viewmodel.fragment.LocalMusicViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by josh on 11/04/16.
 */
@Singleton
@Component(modules = PlayerModule.class)
public interface PlayerComponent extends PlayerBaseComponent{
    void init(AlarmTracksViewModel alarmTracksViewModel);
}
