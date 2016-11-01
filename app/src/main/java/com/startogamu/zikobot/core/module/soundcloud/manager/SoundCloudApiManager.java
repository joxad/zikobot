package com.startogamu.zikobot.core.module.soundcloud.manager;

import android.content.Context;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.module.music.PlayerMusicManager;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudToken;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudTrack;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudUser;
import com.startogamu.zikobot.core.module.soundcloud.resource.SoundCloudApiInterceptor;
import com.startogamu.zikobot.core.module.soundcloud.resource.SoundCloudApiService;

import java.util.ArrayList;



import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by josh on 15/06/16.
 */

public class SoundCloudApiManager {

    public SoundCloudApiService soundCloudApiService;

    private Context context;
    private Retrofit retrofit;


    /**
     * Holder
     */
    private static class SoundCloudApiManagerHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static SoundCloudApiManager instance = new SoundCloudApiManager();
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static SoundCloudApiManager getInstance() {
        return SoundCloudApiManager.SoundCloudApiManagerHolder.instance;
    }

    public void init(Context context) {
        this.context = context;
        SoundCloudRetrofit soundCloudRetrofit = new SoundCloudRetrofit(context.getString(R.string.soundcloud_base_api_url), new SoundCloudApiInterceptor(context));
        this.retrofit = soundCloudRetrofit.retrofit();
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
     * @param id
     * @return
     */
    public Observable<ArrayList<SoundCloudPlaylist>> userPlaylists(final long id) {
        return soundCloudApiService.userPlaylists(id).subscribeOn(Schedulers.io())
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
