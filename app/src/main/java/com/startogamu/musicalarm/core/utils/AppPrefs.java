package com.startogamu.musicalarm.core.utils;

import android.content.Context;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.musicalarm.MusicAlarmApplication;

/***
 *
 */
public class AppPrefs {

    /***
     *
     */
    public static final String ACCESS_CODE = "ACCESS_CODE";

    /***
     *
     */
    public static final String ACCCES_TOKEN = "ACCCES_TOKEN";

    /***
     *
     */
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String SPOTIFY_USER_ID = "SPOTIFY_USER_ID";

    public static void saveRefreshToken(final String refreshToken) {
        Prefs.putString(REFRESH_TOKEN, refreshToken);
    }

    public static String getRefreshToken() {
        return Prefs.getString(REFRESH_TOKEN, "");
    }

    public static void saveAccessCode(final String code) {
        Prefs.putString(ACCESS_CODE, code);
    }

    public static String getAccessCode() {
        return Prefs.getString(ACCESS_CODE, "");
    }

    public static void saveAccessToken(String accessToken) {
        Prefs.putString(ACCCES_TOKEN, accessToken);
    }

    public static String getAcccesToken() {
        return Prefs.getString(ACCCES_TOKEN, "");
    }

    public static void userId(String id) {
        Prefs.putString(SPOTIFY_USER_ID, id);
    }

    public static String spotifyYserId() {
        return Prefs.getString(SPOTIFY_USER_ID, "");
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
