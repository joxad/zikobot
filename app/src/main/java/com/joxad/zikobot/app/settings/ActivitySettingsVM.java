package com.joxad.zikobot.app.settings;

/**
 * Created by josh on 25/03/16.
 */

import android.Manifest;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.general.FragmentWebView;
import com.joxad.zikobot.app.databinding.ActivitySettingsBinding;
import com.joxad.zikobot.app.player.event.EventAccountConnect;
import com.joxad.zikobot.app.soundcloud.SoundCloudLoginActivity;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.db.model.TYPE;
import com.joxad.zikobot.data.module.deezer.DeezerManager;
import com.joxad.zikobot.data.module.soundcloud.manager.SoundCloudApiManager;
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;
import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyRequestToken;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/***
 * {@link ActivitySettingsVM}  make the link between {@link ActivitySettings}
 */
public class ActivitySettingsVM extends ActivityBaseVM<ActivitySettings, ActivitySettingsBinding>  {

    public String TAG = ActivitySettingsVM.class.getSimpleName();
    GoogleAccountCredential mCredential;
    private RxPermissions rxPermissions;
    public ObservableBoolean showSpotifyConnect;
    public ObservableBoolean showSoundCloudConnect;
    public ObservableBoolean showDeezerConnect;
    public ObservableBoolean showYoutubeConnect;
    private static final String[] SCOPES = {YouTubeScopes.YOUTUBE_READONLY};


    public ActivitySettingsVM(ActivitySettings activity, ActivitySettingsBinding binding,@Nullable Bundle saved) {
        super(activity, binding,saved);
    }


    @Override
    public void onCreate(@Nullable Bundle saved) {
        rxPermissions = new RxPermissions(activity);
        showSpotifyConnect = new ObservableBoolean(false);
        showSoundCloudConnect = new ObservableBoolean(false);
        showDeezerConnect = new ObservableBoolean(true);
        showYoutubeConnect = new ObservableBoolean(true);
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setTitle(R.string.activity_my_account);
        binding.switchPermissionPhone.setChecked(rxPermissions.isGranted(Manifest.permission.READ_PHONE_STATE));

    }


    @Override
    public void onResume() {
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
//        if (AppPrefs.getYoutubeAccessToken().equals("")) {
//            showYoutubeConnect.set(true);
//        } else {
//
//        }

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                activity, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        getDeezerMe();
    }


    public void deleteSpotify(@SuppressWarnings("unused") View view) {
        AppPrefs.spotifyUser(null);
        AppPrefs.saveAccessCode(null);
        AppPrefs.saveAccessToken(null);
        showSpotifyConnect.set(true);
        notifyChange();
    }

    public void deleteSoundCloud(@SuppressWarnings("unused") View view) {
        AppPrefs.soundCloudUser(null);
        AppPrefs.saveSoundCloudAccessToken(null);
        notifyChange();
    }

    public void deleteDeezer(@SuppressWarnings("unused") View view) {
        AppPrefs.deezerUser(null);
        DeezerManager.getInstance().logout();
        notifyChange();
    }

    public void deleteYoutube(@SuppressWarnings("unused") View view) {

    }


    public void onButtonYoutubeClick(View view) {
        view.setEnabled(false);

    }

    /***
     * @param view
     */
    public void onButtonSpotifyConnect(@SuppressWarnings("unused") View view) {
        String apiKey = activity.getString(R.string.api_spotify_id);
        String redirect_uri = activity.getString(R.string.api_spotify_callback_web_view);
        String baseUrl = activity.getString(R.string.spotify_web_view);

        FragmentWebView fragmentWebView = FragmentWebView.newInstance(String.format(baseUrl, apiKey, redirect_uri, "user-read-private" + "%20" + "streaming"));
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
    public void onButtonSoundCloudConnect(@SuppressWarnings("unused") View view) {
        activity.startActivity(new Intent(activity, SoundCloudLoginActivity.class));
    }

    /***
     * @param view
     */
    public void onButtonDeezerConnect(@SuppressWarnings("unused") View view) {
        /*DeezerManager.getInstance().login(activity).subscribe(bundle -> {
            getDeezerMe();
        }, throwable -> {
            Logger.e(throwable.getMessage());

        });
*/
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
            EventBus.getDefault().post(new EventAccountConnect(TYPE.SPOTIFY));
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
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });
    }


    /***
     *
     */
    public void getSoundCloudMe() {
        SoundCloudApiManager.getInstance().me().subscribe(soundCloudUser -> {
            binding.cvSoundcloud.tvUser.setText(soundCloudUser.getUsername());
            Glide.with(activity).load(soundCloudUser.getAvatarUrl()).into(binding.cvSoundcloud.ivUser);
            AppPrefs.soundCloudUser(soundCloudUser);
            EventBus.getDefault().post(new EventAccountConnect(TYPE.SOUNDCLOUD));
            showSoundCloudConnect.set(false);
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });
    }


    public void getDeezerMe() {
        DeezerManager.getInstance().current().subscribe(user -> {
            binding.cvDeezer.tvUser.setText(user.getName());
            Glide.with(activity).load(user.getPictureUrl()).into(binding.cvDeezer.ivUser);
            showDeezerConnect.set(false);
            AppPrefs.deezerUser(user);
        }, throwable -> {
            Logger.e(throwable.getMessage());
        });
    }


    public void askPermissionPhone(@SuppressWarnings("unused") View view) {
        binding.switchPermissionPhone.setChecked(binding.switchPermissionPhone.isChecked());
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE).subscribe(granted -> {
            binding.switchPermissionPhone.setChecked(granted);
        }, error -> {
        });
    }

}
