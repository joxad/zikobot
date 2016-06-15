package com.startogamu.zikobot.viewmodel.fragment.spotify;

/**
 * Created by josh on 26/03/16.
 */

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.joxad.android_easy_spotify.Scope;
import com.joxad.android_easy_spotify.SpotifyManager;
import com.joxad.android_easy_spotify.Type;
import com.joxad.easydatabinding.activity.IResult;
import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.connect.EventSpotifyConnect;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.databinding.FragmentConnectSpotifyBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyUser;
import com.startogamu.zikobot.module.spotify_auth.model.SpotifyRequestToken;
import com.startogamu.zikobot.module.spotify_auth.model.SpotifyToken;
import com.startogamu.zikobot.view.activity.ActivityMusic;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyConnect;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

import rx.Subscriber;

/***
 * {@link FragmentConnectVM}  make the link between {@link FragmentSpotifyConnect} and {@link SpotifyManager}
 */
public class FragmentConnectVM extends FragmentBaseVM<FragmentSpotifyConnect, FragmentConnectSpotifyBinding> implements IResult {

    public String TAG = FragmentConnectVM.class.getSimpleName();

    String accessToken;
    String accessCode;

    /***
     * @param fragment
     * @param binding
     */
    public FragmentConnectVM(FragmentSpotifyConnect fragment, FragmentConnectSpotifyBinding binding) {
        super(fragment, binding);

        Injector.INSTANCE.spotifyApi().inject(this);
        Injector.INSTANCE.spotifyAuth().inject(this);
    }


    @Override
    public void init() {

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
    public void onNewIntent(Intent intent) {
        SpotifyManager.onNewIntent(intent);
    }

    /***
     * @param view
     */
    public void onButtonConnectClick(View view) {
        SpotifyManager.loginWithBrowser(fragment.getActivity(), Type.CODE, R.string.api_spotify_callback_web_view,
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
                            fragment.getString(R.string.api_spotify_callback_web_view),
                            fragment.getString(R.string.api_spotify_id),
                            fragment.getString(R.string.api_spotify_secret))).subscribe(new Subscriber<SpotifyToken>() {
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
                AppPrefs.spotifyUser(spotifyUser);
                EventBus.getDefault().post(new EventSpotifyConnect(spotifyUser));
            }
        });
    }

    @Override
    public void destroy() {

    }
}
