package com.startogamu.musicalarm.di.module;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentResolver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by josh on 07/04/16.
 */
@Module
public class ContentResolverModule {

    @Provides
    @Singleton
    public ContentResolver provideContentResolver(final Application context) {
        return context.getContentResolver();
    }
}
