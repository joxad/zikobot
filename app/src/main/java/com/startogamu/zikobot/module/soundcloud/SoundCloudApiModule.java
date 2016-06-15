package com.startogamu.zikobot.module.soundcloud;

import android.content.Context;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.module.retrofit.RetrofitBaseModule;
import com.startogamu.zikobot.module.soundcloud.manager.SoundCloudApiManager;
import com.startogamu.zikobot.module.soundcloud.resource.SoundCloudApiInterceptor;
import com.startogamu.zikobot.module.spotify_api.manager.SpotifyApiManager;
import com.startogamu.zikobot.module.spotify_api.resource.SpotifyApiInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by josh on 15/06/16.
 */
@Module
public class SoundCloudApiModule extends RetrofitBaseModule {

    private SoundCloudApiManager soundcloudApiManager;

    public SoundCloudApiModule(Context context) {
        super(context.getString(R.string.soundcloud_base_api_url), new SoundCloudApiInterceptor(context));
        soundcloudApiManager = new SoundCloudApiManager(context, retrofit());
    }

    @Provides
    @Singleton
    SoundCloudApiManager manager() {
        return soundcloudApiManager;
    }

}

