package com.startogamu.musicalarm.viewmodel;

/**
 * Created by josh on 25/03/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.util.Log;
import android.view.View;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivitySpotifyBinding;
import com.startogamu.musicalarm.di.manager.spotify_api.SpotifyAPIManager;
import com.startogamu.musicalarm.di.manager.spotify_auth.SpotifyAuthManager;
import com.startogamu.musicalarm.model.SpotifyRequestToken;
import com.startogamu.musicalarm.model.SpotifyToken;
import com.startogamu.musicalarm.model.SpotifyUser;
import com.startogamu.musicalarm.view.SpotifyActivity;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import rx.Subscriber;

/***
 * {@link ActivitySpotifyViewModel}  make the link between {@link SpotifyActivity} and {@link SpotifyAPIManager}
 */
public class ActivitySpotifyViewModel extends BaseObservable implements ViewModel {

    private final ActivitySpotifyBinding binding;
    public String TAG = ActivitySpotifyViewModel.class.getSimpleName();
    Activity context;

    String accessCode;
    String accessToken;


    @Inject
    SpotifyAuthManager spotifyAuthManager;
    @Inject
    SpotifyAPIManager spotifyAPIManager;

    public ActivitySpotifyViewModel(Activity context, ActivitySpotifyBinding binding) {
        this.context = context;
        this.binding = binding;
        MusicAlarmApplication.get(context).netComponent.inject(this);

        try {
            new SpotifyManager.Builder()
                    .setContext(context)
                    .setApiKey(context.getString(R.string.api_spotify_id))
                    .setApiCallback(context.getString(R.string.api_spotify_callback))
                    .setConnectionType(AuthenticationResponse.Type.CODE)
                    .setScope(new String[]{"user-read-private", "streaming"})
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Manage the result code of Spotify
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SpotifyManager.onActivityResult(requestCode, resultCode, data);
    }

    /***
     * @param intent
     */
    public void onNewIntent(Intent intent) {
        SpotifyManager.onNewIntent(intent);
    }

    /***
     * @param view
     */
    public void onButtonConnectClick(View view) {

        SpotifyManager.loginWithBrowser(new SpotifyManager.OAuthListener() {
            @Override
            public void onReceived(String code) {
                accessCode = code;
                getTokenFromCode();
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, error);
            }
        });
    }

    private void getTokenFromCode() {
        try {
            spotifyAuthManager.requestToken(
                    new SpotifyRequestToken("authorization_code", accessCode,
                            context.getString(R.string.api_spotify_callback),
                            context.getString(R.string.api_spotify_id),
                            context.getString(R.string.api_spotify_secret)),
                    new Subscriber<SpotifyToken>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, e.getLocalizedMessage());

                        }

                        @Override
                        public void onNext(SpotifyToken spotifyToken) {
                            Log.d(TAG, spotifyToken.toString());
                            accessToken = spotifyToken.toString();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void getMe() {
        spotifyAPIManager.getMe("Bearer " + accessToken, new Subscriber<SpotifyUser>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SpotifyUser spotifyUser) {

            }
        });
    }

    @Override
    public void onDestroy() {
        SpotifyManager.destroy();
    }
}
