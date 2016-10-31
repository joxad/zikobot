package com.oxylane.decathlon.core.prefs;

import android.content.Context;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;

/***
 * {@link AppPrefs} will handle the prefs of the application using {@link Prefs}
 */
public class AppPrefs {


    public static void init(Context context) {
        new Prefs.Builder()
                .setContext(context)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(context.getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
