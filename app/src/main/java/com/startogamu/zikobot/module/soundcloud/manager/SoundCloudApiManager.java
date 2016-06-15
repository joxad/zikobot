package com.startogamu.zikobot.module.soundcloud.manager;

import android.content.Context;

import com.startogamu.zikobot.module.soundcloud.model.SoundCloudTrack;
import com.startogamu.zikobot.module.soundcloud.resource.SoundCloudApiService;

import javax.inject.Singleton;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by josh on 15/06/16.
 */
@Singleton
public class SoundCloudApiManager {

    public SoundCloudApiService soundCloudApiService;

    private Context context;
    private Retrofit retrofit;

    public SoundCloudApiManager(Context context, Retrofit retrofit) {
        this.context = context;
        this.retrofit = retrofit;
        soundCloudApiService = retrofit.create(SoundCloudApiService.class);
    }

    public Observable<SoundCloudTrack> getTrack(){
        return soundCloudApiService.getTrack().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
