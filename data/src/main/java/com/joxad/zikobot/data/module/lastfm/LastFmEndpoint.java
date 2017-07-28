package com.joxad.zikobot.data.module.lastfm;


import com.joxad.zikobot.data.module.lastfm.model.LastFmSearchResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by linux on 7/25/17.
 */

interface LastFmEndpoint {

    @GET("2.0/")
    Observable<LastFmSearchResult> search(@Query("method") final String query, @Query("artist") final String type);

}
