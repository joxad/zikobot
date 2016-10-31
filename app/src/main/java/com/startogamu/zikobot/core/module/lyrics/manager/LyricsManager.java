package com.startogamu.zikobot.module.lyrics.manager;

import android.content.Context;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.module.lyrics.LyricsInterceptor;
import com.startogamu.zikobot.module.lyrics.LyricsService;
import com.startogamu.zikobot.module.lyrics.model.LyricResult;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by josh on 02/08/16.
 */

public class LyricsManager {

    private Context context;
    private Retrofit retrofit;
    protected LyricsService lyricsService;


    /**
     * Holder
     */
    private static class LyricsManagerHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static LyricsManager instance = new LyricsManager();
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static LyricsManager getInstance() {
        return LyricsManager.LyricsManagerHolder.instance;
    }

    public void init(Context context) {
        this.context = context;
        LyricsRetrofit lyricsRetrofit = new LyricsRetrofit(context.getString(R.string.lyrics_wikia_url), new LyricsInterceptor(context));
        this.retrofit = lyricsRetrofit.retrofit();
        lyricsService = retrofit.create(LyricsService.class);
    }

    /**
     * @param artist
     * @param track
     * @return
     */
    public Observable<LyricResult> search(String artist, String track) {
        track = track.replaceAll(" ", "+");
        artist = artist.replaceAll(" ", "+");
        return lyricsService.search(artist, track).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

    }
}
