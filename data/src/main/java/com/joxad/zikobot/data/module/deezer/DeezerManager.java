package com.joxad.zikobot.data.module.deezer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.model.User;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.JsonRequestListener;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by josh on 06/07/16.
 */
public class DeezerManager {

    private DeezerConnect deezerConnect;
    private SessionStore sessionStore;
    private Context context;
    private String appId;

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static DeezerManager getInstance() {
        return DeezerManager.DeezerManagerHolder.instance;
    }

    public void logout() {
        deezerConnect.logout(context);
    }

    public void init(Context context, String appId) {
        this.context = context;
        this.appId = appId;
        deezerConnect = DeezerConnect.forApp(appId).build();
        sessionStore = new SessionStore();
    }

    /***
     * @param activity
     * @param
     */
    public Observable<Bundle> login(Activity activity) {
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
                        sessionStore.save(deezerConnect, context);

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

    public Observable<User> current() {
        sessionStore.restore(deezerConnect, context);
        DeezerRequest request = DeezerRequestFactory.requestCurrentUser();
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                AsyncDeezerTask task = new AsyncDeezerTask(deezerConnect, new JsonRequestListener() {

                    @Override
                    public void onResult(final Object result, final Object requestId) {
                        if (result instanceof User) {
                            subscriber.onNext((User) result);
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

    public Observable<ArrayList<Playlist>> playlists() {
        DeezerRequest request = DeezerRequestFactory.requestCurrentUserPlaylists();
        return Observable.create(new Observable.OnSubscribe<ArrayList<Playlist>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Playlist>> subscriber) {
                AsyncDeezerTask task = new AsyncDeezerTask(deezerConnect, new JsonRequestListener() {

                    @Override
                    public void onResult(final Object result, final Object requestId) {
                        if (result != null) {
                            subscriber.onNext((ArrayList<Playlist>) result);
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
     * @param idPlaylist
     * @return
     */
    public Observable<ArrayList<Track>> playlistTracks(long idPlaylist) {
        DeezerRequest request = DeezerRequestFactory.requestPlaylistTracks(idPlaylist);
        return Observable.create(new Observable.OnSubscribe<ArrayList<Track>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Track>> subscriber) {
                AsyncDeezerTask task = new AsyncDeezerTask(deezerConnect, new JsonRequestListener() {

                    @Override
                    public void onResult(final Object result, final Object requestId) {
                        if (result != null) {
                            subscriber.onNext((ArrayList<Track>) result);
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

    /**
     * Holder
     */
    private static class DeezerManagerHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static DeezerManager instance = new DeezerManager();
    }

}
