package com.joxad.zikobot.data.module.spotify_auth.manager;

import android.content.Context;
import android.util.Base64;

import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.R;
import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyRefreshToken;
import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyRequestToken;
import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyToken;
import com.joxad.zikobot.data.module.spotify_auth.resource.SpotifyAuthInterceptor;
import com.joxad.zikobot.data.module.spotify_auth.resource.SpotifyAuthService;

import java.io.UnsupportedEncodingException;

import retrofit2.Retrofit;
import io.reactivex.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/****
 * {@link SpotifyAuthManager} will handle the connexion to the auth api of spotify
 */

public class SpotifyAuthManager {
    SpotifyAuthService spotifyAuthService;
    Context context;
    Retrofit retrofit;

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static SpotifyAuthManager getInstance() {
        return SpotifyAuthManager.SpotifyAuthManagerHolder.instance;
    }

    public void init(Context context, String appId, String secret) {
        this.context = context;
        SpotifyAuthRetrofit authRetrofit = new SpotifyAuthRetrofit(context.getString(R.string.spotify_base_auth_url),
                new SpotifyAuthInterceptor(appId, secret));

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

    public Observable<SpotifyToken> refreshToken(Context context) throws UnsupportedEncodingException {

        SpotifyRefreshToken spotifyRefreshToken = new SpotifyRefreshToken("refresh_token", AppPrefs.getRefreshToken(),
                context.getString(R.string.api_spotify_callback_web_view),
                context.getString(R.string.api_spotify_id),
                context.getString(R.string.api_spotify_secret));
        String header = "Basic ";
        String id_secret = spotifyRefreshToken.getCliendId() + ":" + spotifyRefreshToken.getClientSecret();
        String b64 = Base64.encodeToString(id_secret.getBytes("UTF-8"), Base64.NO_WRAP);
        header += b64;
        return spotifyAuthService.refreshToken(
                spotifyRefreshToken.getRefreshToken(),
                spotifyRefreshToken.getGrantType(),
                spotifyRefreshToken.getRedirectUri())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


    /**
     * Listener to get info about the refresh token
     */
    public interface Listener {
        void onDone(String newToken, boolean tokenIdentical);
    }

    /**
     * Holder
     */
    private static class SpotifyAuthManagerHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static SpotifyAuthManager instance = new SpotifyAuthManager();
    }


}
