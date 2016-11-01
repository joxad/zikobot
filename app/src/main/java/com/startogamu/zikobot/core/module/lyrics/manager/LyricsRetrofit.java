package com.startogamu.zikobot.core.module.lyrics.manager;

import android.support.annotation.Nullable;

import com.startogamu.zikobot.core.ws.RetrofitBase;

import okhttp3.Interceptor;

/**
 * Created by Jocelyn on 31/10/2016.
 */

public class LyricsRetrofit extends RetrofitBase {
    public LyricsRetrofit(String url, @Nullable Interceptor interceptor) {
        super(url, interceptor);
    }
}
