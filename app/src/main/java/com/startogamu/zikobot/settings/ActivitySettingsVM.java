package com.startogamu.zikobot.settings;

/**
 * Created by josh on 25/03/16.
 */

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.joxad.android_easy_spotify.Scope;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.analytics.AnalyticsManager;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.databinding.ActivitySettingsBinding;

import com.startogamu.zikobot.module.deezer.manager.DeezerManager;
import com.startogamu.zikobot.module.soundcloud.manager.SoundCloudApiManager;
import com.startogamu.zikobot.module.spotify_api.manager.SpotifyApiManager;
import com.startogamu.zikobot.module.spotify_auth.manager.SpotifyAuthManager;
import com.startogamu.zikobot.module.spotify_auth.model.SpotifyRequestToken;
import com.startogamu.zikobot.soundcloud.SoundCloudLoginActivity;
import com.startogamu.zikobot.view.fragment.FragmentWebView;

import java.io.UnsupportedEncodingException;

/***
 * {@link ActivitySettingsVM}  make the link between {@link ActivitySettings} and {@link com.startogamu.zikobot.module.spotify_api.manager.SpotifyApiManager}
 */
public class ActivitySettingsVM extends ActivityBaseVM<ActivitySettings, ActivitySettingsBinding> {

    public String TAG = ActivitySettingsVM.class.getSimpleName();

    public ObservableBoolean showSpotifyConnect;

    public ObservableBoolean showSoundCloudConnect;

    public ObservableBoolean showDeezerConnect;

    public ActivitySettingsVM(ActivitySettings activity, ActivitySettingsBinding binding) {
        super(activity, binding);
    }


    @Override
    public void init() {
        showSpotifyConnect = new ObservableBoolean(false);
        showSoundCloudConnect = new ObservableBoolean(false);
        showDeezerConnect = new ObservableBoolean(true);
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setTitle(R.string.activity_my_account);


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (AppPrefs.getSpotifyAccessCode().equals("")) {
            showSpotifyConnect.set(true);
        } else {
            getSpotifyMe();
        }
        if (AppPrefs.getSoundCloundAccessToken().equals("")) {
            showSoundCloudConnect.set(true);
        } else {
            getSoundCloudMe();
        }
        getDeezerMe();
    }

    /***
     * @param view
     */
    public void onButtonConnectClick(View view) {
        String apiKey = activity.getString(R.string.api_spotify_id);
        String redirect_uri = activity.getString(R.string.api_spotify_callback_web_view);
        String baseUrl = activity.getString(R.string.spotify_web_view);

        FragmentWebView fragmentWebView = FragmentWebView.newInstance(String.format(baseUrl, apiKey, redirect_uri, Scope.USER_READ_PRIVATE + "%20" + Scope.STREAMING));
        fragmentWebView.setIntentListener(intent -> {
            fragmentWebView.dismiss();
            String code = intent.getData().getQueryParameter("code");
            AppPrefs.saveAccessCode(code);
            try {
                getSpotifyTokenFromCode();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        });
        fragmentWebView.show(activity.getSupportFragmentManager(), FragmentWebView.TAG);

    }


    /***
     * @param view
     */
    public void onButtonSoundCloudConnect(View view) {

        activity.startActivity(new Intent(activity, SoundCloudLoginActivity.class));
    }

    /***
     * @param view
     */
    public void onButtonDeezerConnect(View view) {
        DeezerManager.login(activity).subscribe(bundle -> {
            getDeezerMe();
        }, throwable -> {
            Logger.e(throwable.getMessage());

        });

    }


    private void getSoundCloudTokenFromCode() {
        String appId = activity.getString(R.string.soundcloud_id);
        String appSecret = activity.getString(R.string.soundcloud_secret);
        String redirectUrl = activity.getString(R.string.api_soundcloud_callback);
        SoundCloudApiManager.getInstance().token(appId, appSecret, redirectUrl, AppPrefs.getSoundCloundAccesCode(), "authorization_code")
                .subscribe(soundCloudToken -> {
                    AppPrefs.saveSoundCloudAccessToken(soundCloudToken.getAccessToken());
                    getSoundCloudMe();
                }, throwable -> {
                    Logger.e(throwable.getMessage());
                });
    }

    /***
     * @throws UnsupportedEncodingException
     */
    private void getSpotifyTokenFromCode() throws UnsupportedEncodingException {
        SpotifyAuthManager.getInstance().requestToken(new SpotifyRequestToken("authorization_code", AppPrefs.getSpotifyAccessCode(),
                activity.getString(R.string.api_spotify_callback_web_view),
                activity.getString(R.string.api_spotify_id),
                activity.getString(R.string.api_spotify_secret))).subscribe(spotifyToken -> {
            Log.d(TAG, spotifyToken.toString());
            String accessToken = spotifyToken.getAccessToken();
            AppPrefs.saveAccessToken(accessToken);
            AppPrefs.saveRefreshToken(spotifyToken.getRefreshToken());
            getSpotifyMe();
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });
    }

    /***
     *
     */
    public void getSpotifyMe() {
        SpotifyApiManager.getInstance().getMe().subscribe(spotifyUser -> {
            binding.cvSpotify.tvUser.setText(spotifyUser.getDisplayName());
            binding.cvSpotify.tvStatus.setText(spotifyUser.getProduct());
            Glide.with(activity).load(spotifyUser.getImages().get(0).getUrl()).into(binding.cvSpotify.ivUser);
            AppPrefs.spotifyUser(spotifyUser);
            showSpotifyConnect.set(false);
            AnalyticsManager.logConnectSpotify(spotifyUser.getDisplayName());
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });
    }


    /***
     *
     */
    public void getSoundCloudMe() {
        SoundCloudApiManager.getInstance().me().subscribe(soundCloudUser -> {
            binding.cvSoundcloud.tvUserSoundcloud.setText(soundCloudUser.getUserName());
            Glide.with(activity).load(soundCloudUser.getAvatarUrl()).into(binding.cvSoundcloud.ivUserSoundcloud);
            AppPrefs.soundCloudUser(soundCloudUser);
            AnalyticsManager.logConnectSoundCloud(soundCloudUser.getUserName());
            showSoundCloudConnect.set(false);
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });
    }


    public void getDeezerMe() {
        DeezerManager.current().subscribe(user -> {
            binding.cvDeezer.tvUser.setText(user.getName());
            Glide.with(activity).load(user.getPictureUrl()).into(binding.cvDeezer.ivUser);
            showDeezerConnect.set(false);
            AppPrefs.deezerUser(user);
        }, throwable -> {
            Logger.e(throwable.getMessage());
        });
    }

    @Override
    public void destroy() {

    }
}
