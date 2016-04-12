package com.startogamu.musicalarm.module.music;

import android.content.Context;

import com.startogamu.musicalarm.module.music.manager.PlayerMusicManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by josh on 12/04/16.
 */
@Module
public class PlayerModule {

    private PlayerMusicManager playerMusicManager;

    public PlayerModule(Context context) {
        playerMusicManager = new PlayerMusicManager(context);
    }

    @Provides
    @Singleton
    PlayerMusicManager manager() {
        return playerMusicManager;
    }
}
