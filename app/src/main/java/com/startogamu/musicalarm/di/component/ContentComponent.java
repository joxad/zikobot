package com.startogamu.musicalarm.di.component;

import com.startogamu.musicalarm.di.module.ContentResolverModule;
import com.startogamu.musicalarm.viewmodel.fragment.LocalMusicViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by josh on 07/04/16.
 */
@Singleton
@Component(modules = {AppModule.class, ContentResolverModule.class})
public interface ContentComponent {

     void inject(LocalMusicViewModel localMusicViewModel);
}
