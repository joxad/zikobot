package com.startogamu.zikobot.module.spotify_auth.manager;

import android.content.Context;
import android.util.Base64;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.module.localmusic.manager.LocalMusicManager;
import com.startogamu.zikobot.module.spotify_auth.model.SpotifyRefreshToken;
import com.startogamu.zikobot.module.spotify_auth.model.SpotifyRequestToken;
import com.startogamu.zikobot.module.spotify_auth.model.SpotifyToken;
import com.startogamu.zikobot.module.spotify_auth.resource.SpotifyAuthInterceptor;
import com.startogamu.zikobot.module.spotify_auth.resource.SpotifyAuthService;

import java.io.UnsupportedEncodingException;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/****
 * {@link SpotifyAuthManager} will handle the connexion to the auth api of spotify
 */

public class SpotifyAuthManager {
    SpotifyAuthService spotifyAuthService;
    Context context;
    Retrofit retrofit;
    /**
     * Holder
     */
    private static class SpotifyAuthManagerHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static SpotifyAuthManager instance = new SpotifyAuthManager();
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static SpotifyAuthManager getInstance() {
        return SpotifyAuthManager.SpotifyAuthManagerHolder.instance;
    }
    public void init(Context context) {
        this.context = context;
        SpotifyAuthRetrofit authRetrofit = new SpotifyAuthRetrofit(context.getString(R.string.spotify_base_auth_url),
                new SpotifyAuthInterceptor(context.getString(R.string.api_spotify_id), context.getString(R.string.api_spotify_secret)));

        this.retrofit = authRetrofit.retrofit();
        spotifyAuthService = this.retrofit.create(SpotifyAuthService.class);
    }

    public Observable<SpotifyToken> requestToken(final SpotifyRequestToken spotifyRequestToken) throws UnsupportedEncodingException {

        return spotifyAuthService.requestToken(
                spotifyRequestToken.getCode(),
                spotifyRequestToken.getGrantType(),
                spotifyRequestToken.getRedirectUri()
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


    public void refreshToken(Context context, Listener listener) throws UnsupportedEncodingException {

        SpotifyRefreshToken spotifyRefreshToken = new SpotifyRefreshToken("refresh_token", AppPrefs.getRefreshToken(),
                context.getString(R.string.api_spotify_callback_web_view),
                context.getString(R.string.api_spotify_id),
                context.getString(R.string.api_spotify_secret));
        String header = "Basic ";
        String id_secret = spotifyRefreshToken.getCliendId() + ":" + spotifyRefreshToken.getClientSecret();
        String b64 = Base64.encodeToString(id_secret.getBytes("UTF-8"), Base64.NO_WRAP);
        header += b64;
        spotifyAuthService.refreshToken(
                spotifyRefreshToken.getRefreshToken(),
                spotifyRefreshToken.getGrantType(),
                spotifyRefreshToken.getRedirectUri())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SpotifyToken>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SpotifyToken spotifyToken) {
                        AppPrefs.saveAccessToken(spotifyToken.getAccessToken());
                        listener.onDone();
                    }
                });
    }

    public interface Listener {
        void onDone();
    }


}
