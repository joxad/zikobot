package com.startogamu.zikobot.module.soundcloud.manager;

import android.content.Context;

import com.startogamu.zikobot.module.soundcloud.model.SoundCloudToken;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudTrack;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudUser;
import com.startogamu.zikobot.module.soundcloud.resource.SoundCloudApiService;

import java.util.ArrayList;

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

    /***
     *
     * @param id
     * @return
     */
    public Observable<ArrayList<SoundCloudTrack>> userTracks(final long id) {
        return soundCloudApiService.userTracks(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


    /***
     *
     * @param clientId
     * @param clientSecret
     * @param redirectUri
     * @param code
     * @param authorization_code
     * @return
     */
    public Observable<SoundCloudToken> token(final String clientId,
                                             final String clientSecret,
                                             final String redirectUri,
                                             final String code,
                                             final String authorization_code) {
        return soundCloudApiService.token(clientId, clientSecret, redirectUri, code, authorization_code).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<SoundCloudUser> me() {
        return soundCloudApiService.getMe().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
