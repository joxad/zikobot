package com.startogamu.musicalarm.module.content_resolver;

import android.content.ContentResolver;
import android.content.Context;

import com.startogamu.musicalarm.module.content_resolver.manager.LocalMusicManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContentResolverModule  {
    private final LocalMusicManager localMusicManager;
    private ContentResolver contentResolver;
    public ContentResolverModule(Context context) {
        this.contentResolver = context.getContentResolver();
        this.localMusicManager = new LocalMusicManager(contentResolver);
    }


    @Provides
    @Singleton
    LocalMusicManager localMusicManager() {
        return localMusicManager;
    }

    @Provides
    @Singleton
    ContentResolver contentResolver() {
        return contentResolver;
    }
}
