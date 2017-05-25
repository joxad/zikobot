package com.joxad.zikobot.app.settings;

/**
 * Created by josh on 25/03/16.
 */

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.IResult;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.general.FragmentWebView;
import com.joxad.zikobot.app.databinding.ActivitySettingsBinding;
import com.joxad.zikobot.app.player.event.EventAccountConnect;
import com.joxad.zikobot.app.soundcloud.SoundCloudLoginActivity;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.model.TYPE;
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
public class ActivitySettingsVM extends ActivityBaseVM<ActivitySettings, ActivitySettingsBinding> implements IResult {

    public String TAG = ActivitySettingsVM.class.getSimpleName();
    GoogleAccountCredential mCredential;
    private RxPermissions rxPermissions;
    public ObservableBoolean showSpotifyConnect;
    public ObservableBoolean showSoundCloudConnect;
    public ObservableBoolean showDeezerConnect;
    public ObservableBoolean showYoutubeConnect;
    private static final String[] SCOPES = {YouTubeScopes.YOUTUBE_READONLY};
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    String accountName;

    public ActivitySettingsVM(ActivitySettings activity, ActivitySettingsBinding binding) {
        super(activity, binding);
    }


    @Override
    public void onCreate() {
        rxPermissions = new RxPermissions(activity);
        showSpotifyConnect = new ObservableBoolean(false);
        showSoundCloudConnect = new ObservableBoolean(false);
        showDeezerConnect = new ObservableBoolean(true);
        showYoutubeConnect = new ObservableBoolean(true);
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setTitle(R.string.activity_my_account);


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
        getResultsFromApi();

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


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            // mOutputText.setText("No network connection available.");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    private void chooseAccount() {
        rxPermissions.request(Manifest.permission.GET_ACCOUNTS).subscribe(granted -> {
//            String accountName = getPreferences(Context.MODE_PRIVATE)
//                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                activity.startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        });
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(activity);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(activity);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                activity,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != Activity.RESULT_OK) {

                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String token = data.getStringExtra(AccountManager.KEY_AUTHTOKEN);
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    //TODO AppPrefs.saveYoutube();

                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }
}
