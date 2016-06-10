package com.startogamu.zikobot.core.utils;

import android.content.Context;
import android.content.ContextWrapper;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyUser;

/***
 * {@link AppPrefs} will handle the prefs of the application using {@link Prefs}
 */
public class AppPrefs {

    static Gson gson = new Gson();

    public static final String FIRST_START = "FIRST_START";
    /***
     *
     */
    public static final String SPOTIFY_ACCESS_CODE = "SPOTIFY_ACCESS_CODE";

    /***
     *
     */
    public static final String SPOTIFY_ACCESS_TOKEN = "SPOTIFY_ACCESS_TOKEN";

    /***
     *
     */
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String SPOTIFY_USER = "SPOTIFY_USER";


    public static boolean isFirstStart() {
        return Prefs.getBoolean(FIRST_START, true);
    }

    public static void saveFirstStart(final boolean state) {
        Prefs.putBoolean(FIRST_START, state);
    }

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
        return gson.fromJson(user, SpotifyUser.class);
    }

    public static void init(Context context) {
        new Prefs.Builder()
                .setContext(context)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(context.getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
