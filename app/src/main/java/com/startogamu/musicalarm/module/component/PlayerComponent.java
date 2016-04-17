package com.startogamu.musicalarm.module.component;

import com.startogamu.musicalarm.core.receiver.StopReceiver;
import com.startogamu.musicalarm.core.service.AlarmService;
import com.startogamu.musicalarm.module.music.PlayerBaseComponent;
import com.startogamu.musicalarm.module.music.PlayerModule;
import com.startogamu.musicalarm.viewmodel.fragment.AlarmTracksVM;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by josh on 11/04/16.
 */
@Singleton
@Component(modules = PlayerModule.class)
public interface PlayerComponent extends PlayerBaseComponent{
    void init(AlarmTracksVM alarmTracksVM);

    void inject(AlarmService alarmService);

    void inject(StopReceiver stopReceiver);
}
