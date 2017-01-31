package com.joxad.zikobot.data.module.soundcloud.manager;

import android.support.annotation.Nullable;

import com.joxad.zikobot.data.ws.RetrofitBase;

import okhttp3.Interceptor;

/**
 * Created by Jocelyn on 31/10/2016.
 */

public class SoundCloudRetrofit extends RetrofitBase {
    public SoundCloudRetrofit(String url, @Nullable Interceptor interceptor) {
        super(url, interceptor);
    }
}
