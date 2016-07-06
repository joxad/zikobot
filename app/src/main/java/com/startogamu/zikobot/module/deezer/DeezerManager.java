package com.startogamu.zikobot.module.deezer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.startogamu.zikobot.R;

/**
 * Created by josh on 06/07/16.
 */
public class DeezerManager {

    private static DeezerConnect deezerConnect;
    private static SessionStore sessionStore;

    private static void init(Context context, int appId) {
        deezerConnect = DeezerConnect.forApp(context.getString(R.string.deezer_id)).build();
        sessionStore = new SessionStore();
        sessionStore.save(deezerConnect, context);
    }


    public static void login(Activity activity) {
        // The set of Deezer Permissions needed by the app
        String[] permissions = new String[]{
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY};
// The listener for authentication events
        DialogListener listener = new DialogListener() {
            public void onComplete(Bundle values) {
            }

            public void onCancel() {
            }

            public void onException(Exception e) {
            }
        };
// Launches the authentication process
        deezerConnect.authorize(activity, permissions, listener);
    }

    /***
     *
     */
    public static class Builder {

        private Context context;

        @StringRes
        private int appIdRes = -1;

        /***
         * @param context
         * @return
         */
        public Builder context(final Context context) {
            this.context = context;
            return this;
        }

        /***
         * @param appIdRes
         * @return
         */
        public Builder appId(final int appIdRes) {
            this.appIdRes = appIdRes;
            return this;
        }


        /***
         *
         */
        public void build() throws Exception {
            if (this.context == null)
                throw new Exception("Please set the Context");
            if (this.appIdRes == -1)
                throw new Exception("Please set the appIdRes");
            DeezerManager.init(this.context, this.appIdRes);
        }

    }

}
