package com.startogamu.musicalarm.module.content_resolver;

import android.content.ContentResolver;

import com.startogamu.musicalarm.module.content_resolver.manager.LocalMusicManager;

/**
 * Created by josh on 11/04/16.
 */
public interface ContentResolverBaseComponent {
     ContentResolver contentResolver();
    LocalMusicManager localMusicManager();
}
