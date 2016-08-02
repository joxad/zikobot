package com.startogamu.zikobot.module.lyrics;

import android.content.Context;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.module.retrofit.RetrofitBaseModule;
import com.startogamu.zikobot.module.soundcloud.resource.SoundCloudApiInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by josh on 02/08/16.
 */
@Module
public class LyricsApiModule extends RetrofitBaseModule {

    private LyricsManager lyricsManager;

    public LyricsApiModule(Context context) {
        super(context.getString(R.string.lyrics_wikia_url), new LyricsInterceptor(context));
        lyricsManager = new LyricsManager(context, retrofit());
    }

    @Provides
    @Singleton
    LyricsManager manager() {
        return lyricsManager;
    }
}
