package com.startogamu.musicalarm.di.component;


import com.startogamu.musicalarm.di.module.AppModule;
import com.startogamu.musicalarm.viewmodel.ActivityAlarmViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by josh on 24/03/16.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface NetComponent {

    void inject(ActivityAlarmViewModel activityAlarmViewModel);

}
