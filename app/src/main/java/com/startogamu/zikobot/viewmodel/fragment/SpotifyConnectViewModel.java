package com.startogamu.zikobot.viewmodel.fragment;

/**
 * Created by josh on 26/03/16.
 */

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.joxad.android_easy_spotify.Scope;
import com.joxad.android_easy_spotify.SpotifyManager;
import com.joxad.android_easy_spotify.Type;
import com.joxad.easydatabinding.base.IVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.databinding.FragmentConnectSpotifyBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyUser;
import com.startogamu.zikobot.module.spotify_auth.model.SpotifyRequestToken;
import com.startogamu.zikobot.module.spotify_auth.model.SpotifyToken;
import com.startogamu.zikobot.view.activity.ActivityMusic;
import com.startogamu.zikobot.view.fragment.SpotifyConnectFragment;

import java.io.UnsupportedEncodingException;

import rx.Subscriber;

/***
 * {@link SpotifyConnectViewModel}  make the link between {@link SpotifyConnectFragment} and {@link SpotifyManager}
 */
public class SpotifyConnectViewModel extends BaseObservable implements IVM {

    public String TAG = SpotifyConnectViewModel.class.getSimpleName();
    SpotifyConnectFragment context;
    private FragmentConnectSpotifyBinding binding;

    String accessToken;
    String accessCode;

    /***
     * @param fragment
     * @param binding
     */
    public SpotifyConnectViewModel(SpotifyConnectFragment fragment, FragmentConnectSpotifyBinding binding) {
        this.binding = binding;
        this.context = fragment;
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
        SpotifyManager.loginWithBrowser(context.getActivity(), Type.CODE, R.string.api_spotify_callback_musics,
                new String[]{Scope.USER_READ_PRIVATE, Scope.STREAMING}, new SpotifyManager.OAuthListener() {
                    @Override
                    public void onReceived(String code) {
                        accessCode = code;
                        AppPrefs.saveAccessCode(code);
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
            Injector.INSTANCE.spotifyAuth().manager().requestToken(
                    new SpotifyRequestToken("authorization_code", accessCode,
                            context.getString(R.string.api_spotify_callback_musics),
                            context.getString(R.string.api_spotify_id),
                            context.getString(R.string.api_spotify_secret))).subscribe(new Subscriber<SpotifyToken>() {
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
                            AppPrefs.saveRefreshToken(spotifyToken.getRefreshToken());
                            AppPrefs.saveAccessToken(spotifyToken.getAccessToken());
                            getMe();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /***
     *
     */
    public void getMe() {
        Injector.INSTANCE.spotifyApi().manager().getMe().subscribe(new Subscriber<SpotifyUser>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(binding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNext(SpotifyUser spotifyUser) {
                AppPrefs.userId(spotifyUser.id);
                ((ActivityMusic) context.getActivity()).loadSpotifyMusicFragment();
            }
        });
    }



    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}