package com.joxad.zikobot.data.module.soundcloud.manager;

import android.content.Context;

import com.joxad.zikobot.data.R;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudToken;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudTrack;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudUser;
import com.joxad.zikobot.data.module.soundcloud.resource.SoundCloudApiEndpoint;
import com.joxad.zikobot.data.module.soundcloud.resource.SoundCloudApiInterceptor;

import java.util.ArrayList;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by josh on 15/06/16.
 */

public class SoundCloudApiManager {

    public SoundCloudApiEndpoint soundCloudApiEndpoint;

    private Context context;
    private Retrofit retrofit;

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
        soundCloudApiEndpoint = retrofit.create(SoundCloudApiEndpoint.class);
    }

    /***
     * @param id
     * @return
     */
    public Observable<ArrayList<SoundCloudTrack>> userTracks(final long id) {
        return soundCloudApiEndpoint.userTracks(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /***
     * @param filter
     * @return
     */
    public Observable<ArrayList<SoundCloudTrack>> search(final String filter) {
        return soundCloudApiEndpoint.search(filter).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /***
     * @param id
     * @return
     */
    public Observable<SoundCloudUser> userById(final long id) {
        return soundCloudApiEndpoint.user(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /***
     * @param search
     * @return
     */
    public Observable<ArrayList<SoundCloudUser>> searchUsers(final String search) {
        return soundCloudApiEndpoint.searchUser(search).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /***
     * @param search
     * @return
     */
    public Observable<ArrayList<SoundCloudPlaylist>> searchPlaylists(final String search) {
        return soundCloudApiEndpoint.searchPlaylist(search).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


    /***
     * @param id
     * @return
     */
    public Observable<ArrayList<SoundCloudTrack>> favoriteTracks(final long id) {
        return soundCloudApiEndpoint.userFavoriteTracks(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
    /***
     * @param id
     * @return
     */
    public Observable<ArrayList<SoundCloudPlaylist>> userPlaylists(final long id) {
        return soundCloudApiEndpoint.userPlaylists(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /***
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
        return soundCloudApiEndpoint.token(clientId, clientSecret, redirectUri, code, authorization_code).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<SoundCloudUser> me() {
        return soundCloudApiEndpoint.getMe().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * Holder
     */
    private static class SoundCloudApiManagerHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static SoundCloudApiManager instance = new SoundCloudApiManager();
    }
}
