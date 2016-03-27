package com.startogamu.musicalarm.viewmodel.fragment;

/**
 * Created by josh on 26/03/16.
 */

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentConnectSpotifyBinding;
import com.startogamu.musicalarm.di.manager.spotify_api.SpotifyAPIManager;
import com.startogamu.musicalarm.di.manager.spotify_auth.SpotifyAuthManager;
import com.startogamu.musicalarm.model.spotify.SpotifyRequestToken;
import com.startogamu.musicalarm.model.spotify.SpotifyToken;
import com.startogamu.musicalarm.model.spotify.SpotifyUser;
import com.startogamu.musicalarm.utils.SpotifyPrefs;
import com.startogamu.musicalarm.view.activity.MusicActivity;
import com.startogamu.musicalarm.view.fragment.SpotifyConnectFragment;
import com.startogamu.musicalarm.view.fragment.SpotifyMusicFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import rx.Subscriber;

/***
 * {@link SpotifyConnectViewModel}  make the link between {@link SpotifyConnectFragment} and {@link SpotifyManager}
 */
public class SpotifyConnectViewModel extends BaseObservable implements ViewModel {

    public String TAG = SpotifyConnectViewModel.class.getSimpleName();
    SpotifyConnectFragment context;
    private FragmentConnectSpotifyBinding binding;

    String accessToken;
    String accessCode;
    @Inject
    SpotifyAuthManager spotifyAuthManager;
    @Inject
    SpotifyAPIManager spotifyAPIManager;

    /***
     * @param fragment
     * @param binding
     */
    public SpotifyConnectViewModel(SpotifyConnectFragment fragment, FragmentConnectSpotifyBinding binding) {
        this.binding = binding;
        this.context = fragment;
        MusicAlarmApplication.get(context.getContext()).netComponent.inject(this);
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
                Prefs.putString(SpotifyPrefs.SPOTIFY_CODE, code);
                getTokenFromCode(code);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, error);
            }
        });
    }


    /***
     * @param accessCode
     */
    private void getTokenFromCode(final String accessCode) {
        try {
            spotifyAuthManager.requestToken(
                    new SpotifyRequestToken("authorization_code", accessCode,
                            context.getString(R.string.api_spotify_callback_musics),
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
                            Prefs.putString(SpotifyPrefs.SPOTIFY_TOKEN, accessToken);
                            getMe(accessToken);
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /***
     * @param accessToken
     */
    public void getMe(final String accessToken) {
        spotifyAPIManager.getMe(accessToken, new Subscriber<SpotifyUser>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(binding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNext(SpotifyUser spotifyUser) {
                ((MusicActivity)context.getActivity()).loadSpotifyMusicFragment();
            }
        });
    }

    @Override
    public void onDestroy() {
        SpotifyManager.destroy();
    }


}
