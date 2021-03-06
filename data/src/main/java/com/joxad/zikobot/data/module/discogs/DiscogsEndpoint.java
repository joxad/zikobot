package com.joxad.zikobot.data.module.discogs;

import com.joxad.zikobot.data.module.discogs.model.DiscogsBaseSearchResult;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by linux on 7/25/17.
 */

interface DiscogsEndpoint {

    @GET("database/search")
    Observable<DiscogsBaseSearchResult> search(@Query("q") final String query, @Query("type") final String type);

}
