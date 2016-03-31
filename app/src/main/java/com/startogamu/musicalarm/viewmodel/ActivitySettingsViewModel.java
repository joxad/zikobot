package com.startogamu.musicalarm.viewmodel;

/**
 * Created by josh on 25/03/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.joxad.android_easy_spotify.SpotifyManager;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivitySettingsBinding;
import com.startogamu.musicalarm.di.manager.spotify_api.SpotifyAPIManager;
import com.startogamu.musicalarm.di.manager.spotify_auth.SpotifyAuthManager;
import com.startogamu.musicalarm.model.spotify.SpotifyRequestToken;
import com.startogamu.musicalarm.model.spotify.SpotifyToken;
import com.startogamu.musicalarm.model.spotify.SpotifyUser;
import com.startogamu.musicalarm.utils.SpotifyPrefs;
import com.startogamu.musicalarm.view.activity.SettingsActivity;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import rx.Subscriber;

/***
 * {@link ActivitySettingsViewModel}  make the link between {@link SettingsActivity} and {@link SpotifyAPIManager}
 */
public class ActivitySettingsViewModel extends BaseObservable implements ViewModel {

    private final ActivitySettingsBinding binding;
    public String TAG = ActivitySettingsViewModel.class.getSimpleName();
    Activity context;

    String accessCode;
    String accessToken;

    @Inject
    SpotifyAuthManager spotifyAuthManager;
    @Inject
    SpotifyAPIManager spotifyAPIManager;

    public ActivitySettingsViewModel(Activity context, ActivitySettingsBinding binding) {
        this.context = context;
        this.binding = binding;
        MusicAlarmApplication.get(context).netComponent.inject(this);

        try {
            new SpotifyManager.Builder()
                    .setContext(context)
                    .setApiKey(context.getString(R.string.api_spotify_id))
                    .setApiCallback(context.getString(R.string.api_spotify_callback_settings))
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
                            context.getString(R.string.api_spotify_callback_settings),
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
                            accessToken = spotifyToken.getAccessToken();
                            SpotifyPrefs.saveAccessToken(accessToken);
                            getMe();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void getMe() {
        spotifyAPIManager.getMe(new Subscriber<SpotifyUser>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onNext(SpotifyUser spotifyUser) {
                binding.tvUser.setText(spotifyUser.getDisplayName());
                Glide.with(context).load(spotifyUser.getImages().get(0).getUrl()).into(binding.ivUser);
            }
        });
    }

    @Override
    public void onDestroy() {
        SpotifyManager.destroy();
    }


}
