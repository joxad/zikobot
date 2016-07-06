package com.startogamu.zikobot.module.deezer.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.User;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.startogamu.zikobot.R;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by josh on 06/07/16.
 */
public class DeezerManager {

    private static DeezerConnect deezerConnect;
    private static SessionStore sessionStore;
    private static Context context;
    private static int appId;

    private static void init(Context context, int appId) {
        DeezerManager.context = context;
        DeezerManager.appId = appId;
        deezerConnect = DeezerConnect.forApp(context.getString(R.string.deezer_id)).build();
        sessionStore = new SessionStore();
        sessionStore.save(deezerConnect, context);

    }

    /***
     * @param activity
     * @param
     */
    public static Observable<Bundle> login(Activity activity) {
        // The set of Deezer Permissions needed by the app
        String[] permissions = new String[]{
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.OFFLINE_ACCESS,
                Permissions.LISTENING_HISTORY};

        return Observable.create(new Observable.OnSubscribe<Bundle>() {
            @Override
            public void call(Subscriber subscriber) {
                deezerConnect.authorize(activity, permissions, new DialogListener() {
                    @Override
                    public void onComplete(Bundle bundle) {
                        subscriber.onNext(bundle);
                    }

                    @Override
                    public void onCancel() {
                        subscriber.onError(new Throwable("Cancel"));
                    }

                    @Override
                    public void onException(Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }

    public static Observable<User> current() {

        DeezerRequest request = DeezerRequestFactory.requestCurrentUser();
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                AsyncDeezerTask task = new AsyncDeezerTask(deezerConnect, new JsonRequestListener() {

                    @Override
                    public void onResult(final Object result, final Object requestId) {
                        if (result instanceof User) {
                            subscriber.onNext((User)result);
                        } else {
                            subscriber.onError(new Throwable("Error of parsing"));
                        }
                    }

                    @Override
                    public void onUnparsedResult(final String response, final Object requestId) {
                        subscriber.onError(new DeezerError("Unparsed reponse"));
                    }


                    @Override
                    public void onException(final Exception exception, final Object requestId) {
                        subscriber.onError(exception);
                    }
                });

                task.execute(request);
            }
        });

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
