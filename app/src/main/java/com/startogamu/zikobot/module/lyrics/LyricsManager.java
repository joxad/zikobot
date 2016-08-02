package com.startogamu.zikobot.module.lyrics;

import android.content.Context;

import com.startogamu.zikobot.module.lyrics.model.LyricResult;
import com.startogamu.zikobot.module.lyrics.model.Result;

import javax.inject.Singleton;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by josh on 02/08/16.
 */
@Singleton
public class LyricsManager {

    private final Context context;
    private final Retrofit retrofit;
    protected LyricsService lyricsService;

    public LyricsManager(Context context, Retrofit retrofit) {
        this.context = context;
        this.retrofit = retrofit;
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
