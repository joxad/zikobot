package com.joxad.zikobot.app.core.utils;

import android.content.Context;
import android.content.ContextWrapper;

import com.deezer.sdk.model.User;
import com.google.gson.Gson;
import com.joxad.zikobot.app.core.module.soundcloud.model.SoundCloudUser;
import com.joxad.zikobot.app.core.module.spotify_api.model.SpotifyUser;
import com.pixplicity.easyprefs.library.Prefs;

/***
 * {@link AppPrefs} will handle the prefs of the application using {@link Prefs}
 */
public class AppPrefs {


    static Gson gson = new Gson();

    public static final String FIRST_START = "FIRST_START";


    /***
     * SPOTIFY
     */
    public static final String SPOTIFY_ACCESS_CODE = "SPOTIFY_ACCESS_CODE";
    public static final String SPOTIFY_ACCESS_TOKEN = "SPOTIFY_ACCESS_TOKEN";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String SPOTIFY_USER = "SPOTIFY_USER";
    private static final String DEEZER_USER = "DEEZER_USER";

    public static void saveRefreshToken(final String refreshToken) {
        Prefs.putString(REFRESH_TOKEN, refreshToken);
    }

    public static String getRefreshToken() {
        return Prefs.getString(REFRESH_TOKEN, "");
    }

    public static void saveAccessCode(final String code) {
        Prefs.putString(SPOTIFY_ACCESS_CODE, code);
    }

    public static String getSpotifyAccessCode() {
        return Prefs.getString(SPOTIFY_ACCESS_CODE, "");
    }

    public static void saveAccessToken(String accessToken) {
        Prefs.putString(SPOTIFY_ACCESS_TOKEN, accessToken);
    }

    public static String getSpotifyAccessToken() {
        return Prefs.getString(SPOTIFY_ACCESS_TOKEN, "");
    }

    public static void spotifyUser(SpotifyUser spotifyUser) {
        Prefs.putString(SPOTIFY_USER, gson.toJson(spotifyUser));
    }

    public static SpotifyUser spotifyUser() {
        String user = Prefs.getString(SPOTIFY_USER, "");
        if (user.equals(""))
            return null;
        return gson.fromJson(user, SpotifyUser.class);
    }

    public static void deezerUser(User deezerUser) {
        Prefs.putString(DEEZER_USER, gson.toJson(deezerUser));
    }

    public static User deezerUser() {
        String user = Prefs.getString(DEEZER_USER, "");
        if (user.equals(""))
            return null;
        return gson.fromJson(user, User.class);
    }

    /***
     *
     */
    public static final String SOUNDCLOUD_ACCESS_TOKEN = "SOUNDCLOUD_ACCESS_TOKEN";
    private static final String SOUNDCLOUD_ACCESS_CODE = "SOUNDCLOUD_ACCESS_CODE";
    private static final String SOUNDCLOUD_USER = "SOUNDCLOUD_USER";

    public static void saveSoundCloudAccessToken(String accessToken) {
        Prefs.putString(SOUNDCLOUD_ACCESS_TOKEN, accessToken);
    }

    public static String getSoundCloundAccessToken() {
        return Prefs.getString(SOUNDCLOUD_ACCESS_TOKEN, "");
    }

    public static void soundCloudUser(SoundCloudUser soundCloudUser) {
        Prefs.putString(SOUNDCLOUD_USER, gson.toJson(soundCloudUser));
    }

    public static SoundCloudUser soundCloudUser() {
        String user = Prefs.getString(SOUNDCLOUD_USER, "");
        return gson.fromJson(user, SoundCloudUser.class);
    }

    public static void saveSoundCloudAccessCode(String code) {
        Prefs.putString(SOUNDCLOUD_ACCESS_CODE, code);
    }

    public static String getSoundCloundAccesCode() {
        return Prefs.getString(SOUNDCLOUD_ACCESS_CODE, "");
    }

    /*******************************************************************************************/

    public static boolean isFirstStart() {
        return Prefs.getBoolean(FIRST_START, true);
    }

    public static void saveFirstStart(final boolean state) {
        Prefs.putBoolean(FIRST_START, state);
    }


    /***
     * Init the prefs
     *
     * @param context
     */
    public static void init(Context context) {
        new Prefs.Builder()
                .setContext(context)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(context.getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

}
