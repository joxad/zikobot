package com.startogamu.musicalarm.module.spotify_auth.manager;

import android.content.Context;
import android.util.Base64;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.AppPrefs;

import com.startogamu.musicalarm.module.spotify_auth.object.SpotifyRefreshToken;
import com.startogamu.musicalarm.module.spotify_auth.object.SpotifyRequestToken;
import com.startogamu.musicalarm.module.spotify_auth.object.SpotifyToken;
import com.startogamu.musicalarm.module.spotify_auth.resource.SpotifyAuthService;

import java.io.UnsupportedEncodingException;

import javax.inject.Singleton;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/****
 * {@link SpotifyAuthManager} will handle the connexion to the auth api of spotify
 */
@Singleton
public class SpotifyAuthManager {
    SpotifyAuthService spotifyAuthService;
    Context context;
    Retrofit retrofit;

    public SpotifyAuthManager(Context context, Retrofit retrofit) {
        this.context = context;
        this.retrofit = retrofit;
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
                context.getString(R.string.api_spotify_callback_musics),
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
        public void onDone();
    }


}
