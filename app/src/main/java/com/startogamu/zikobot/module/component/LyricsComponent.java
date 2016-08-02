package com.startogamu.zikobot.module.component;

import com.startogamu.zikobot.module.lyrics.LyricsApiModule;
import com.startogamu.zikobot.module.lyrics.LyricsBaseComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by josh on 02/08/16.
 */
@Singleton
@Component(modules = LyricsApiModule.class)
public interface LyricsComponent extends LyricsBaseComponent {


}
