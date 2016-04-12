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
import com.joxad.android_easy_spotify.Scope;
import com.joxad.android_easy_spotify.SpotifyManager;
import com.joxad.android_easy_spotify.Type;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.AppPrefs;
import com.startogamu.musicalarm.databinding.ActivitySettingsBinding;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyUser;
import com.startogamu.musicalarm.module.spotify_auth.object.SpotifyRequestToken;
import com.startogamu.musicalarm.module.spotify_auth.object.SpotifyToken;
import com.startogamu.musicalarm.view.activity.SettingsActivity;

import java.io.UnsupportedEncodingException;

import rx.Subscriber;

/***
 * {@link ActivitySettingsViewModel}  make the link between {@link SettingsActivity} and {@link com.startogamu.musicalarm.module.spotify_api.manager.SpotifyApiManager}
 */
public class ActivitySettingsViewModel extends BaseObservable implements ViewModel {

    private final ActivitySettingsBinding binding;
    public String TAG = ActivitySettingsViewModel.class.getSimpleName();
    Activity context;

    String accessCode;
    String accessToken;


    public ActivitySettingsViewModel(Activity context, ActivitySettingsBinding binding) {
        this.context = context;
        this.binding = binding;
        Injector.INSTANCE.spotifyApi().inject(this);
        Injector.INSTANCE.spotifyAuth().inject(this);
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

        SpotifyManager.loginWithBrowser(context, Type.CODE, R.string.api_spotify_callback_settings, new String[]{Scope.USER_READ_PRIVATE, Scope.STREAMING}, new SpotifyManager.OAuthListener() {
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
            Injector.INSTANCE.spotifyAuth().manager().requestToken(
                    new SpotifyRequestToken("authorization_code", accessCode,
                            context.getString(R.string.api_spotify_callback_settings),
                            context.getString(R.string.api_spotify_id),
                            context.getString(R.string.api_spotify_secret))).subscribe(
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
                            AppPrefs.saveAccessToken(accessToken);
                            getMe();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void getMe() {
        Injector.INSTANCE.spotifyApi().manager().getMe().subscribe(new Subscriber<SpotifyUser>() {
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

    }


}
