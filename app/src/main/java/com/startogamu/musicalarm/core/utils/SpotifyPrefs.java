package com.startogamu.musicalarm.core.utils;

import com.pixplicity.easyprefs.library.Prefs;

/***
 *
 */
public class SpotifyPrefs {

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

    public static void saveUser(String id) {
        Prefs.putString(SPOTIFY_USER_ID, id);
    }

    public static String getSpotifyUserId() {
        return Prefs.getString(SPOTIFY_USER_ID, "");
    }
}
