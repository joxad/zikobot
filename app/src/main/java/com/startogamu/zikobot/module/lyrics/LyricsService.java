package com.startogamu.zikobot.module.lyrics;

import com.startogamu.zikobot.module.lyrics.model.LyricResult;
import com.startogamu.zikobot.module.lyrics.model.Result;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by josh on 02/08/16.
 */
public interface LyricsService {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("wikia.php?controller=LyricsApi&method=getSong")
    Observable<LyricResult> search(@Query(value = "artist",encoded = true) final String artist,
                                   @Query(value = "song",encoded = true) final String song);

}
