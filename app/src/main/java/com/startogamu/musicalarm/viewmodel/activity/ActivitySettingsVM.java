package com.startogamu.musicalarm.viewmodel.activity;

/**
 * Created by josh on 25/03/16.
 */

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.joxad.android_easy_spotify.Scope;
import com.joxad.android_easy_spotify.SpotifyManager;
import com.joxad.android_easy_spotify.Type;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.INewIntent;
import com.joxad.easydatabinding.activity.IResult;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.AppPrefs;
import com.startogamu.musicalarm.databinding.ActivitySettingsBinding;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.module.spotify_api.model.SpotifyUser;
import com.startogamu.musicalarm.module.spotify_auth.model.SpotifyRequestToken;
import com.startogamu.musicalarm.module.spotify_auth.model.SpotifyToken;
import com.startogamu.musicalarm.view.activity.ActivityMusic;
import com.startogamu.musicalarm.view.activity.ActivitySettings;

import java.io.UnsupportedEncodingException;

import rx.Subscriber;

/***
 * {@link ActivitySettingsVM}  make the link between {@link ActivitySettings} and {@link com.startogamu.musicalarm.module.spotify_api.manager.SpotifyApiManager}
 */
public class ActivitySettingsVM extends ActivityBaseVM<ActivitySettings, ActivitySettingsBinding> implements IResult, INewIntent {

    public String TAG = ActivitySettingsVM.class.getSimpleName();

    String accessCode;
    String accessToken;

    public ObservableBoolean showSpotifyConnect;

    public ActivitySettingsVM(ActivitySettings activity, ActivitySettingsBinding binding) {
        super(activity, binding);
        Injector.INSTANCE.spotifyApi().inject(this);
        Injector.INSTANCE.spotifyAuth().inject(this);
    }


    @Override
    public void init() {
        showSpotifyConnect = new ObservableBoolean(false);
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setTitle(R.string.action_settings);
        if (AppPrefs.getSpotifyAccessCode().equals("")) {
            showSpotifyConnect.set(true);
        } else {
            getMe();
        }
    }


    /***
     * Manage the result code of Spotify
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SpotifyManager.onActivityResult(requestCode, resultCode, data);
    }

    /***
     * @param intent
     */
    @Override
    public void onNewIntent(Intent intent) {
        SpotifyManager.onNewIntent(intent);
    }

    /***
     * @param view
     */
    public void onButtonConnectClick(View view) {

        SpotifyManager.loginWithBrowser(activity, Type.CODE, R.string.api_spotify_callback_settings, new String[]{Scope.USER_READ_PRIVATE, Scope.STREAMING}, new SpotifyManager.OAuthListener() {
            @Override
            public void onReceived(String code) {
                accessCode = code;
                AppPrefs.saveAccessCode(accessCode);
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
                            activity.getString(R.string.api_spotify_callback_settings),
                            activity.getString(R.string.api_spotify_id),
                            activity.getString(R.string.api_spotify_secret))).subscribe(
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
                            AppPrefs.saveRefreshToken(spotifyToken.getRefreshToken());

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
                Glide.with(activity).load(spotifyUser.getImages().get(0).getUrl()).into(binding.ivUser);
                AppPrefs.userId(spotifyUser.getId());
                showSpotifyConnect.set(false);

            }
        });
    }


    @Override
    public void destroy() {

    }
}
