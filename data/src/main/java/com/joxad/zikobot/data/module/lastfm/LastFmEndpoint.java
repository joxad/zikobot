package com.joxad.zikobot.data.module.lastfm;


import com.joxad.zikobot.data.module.lastfm.model.LastFmSearchResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Observable;

/**
 * Created by linux on 7/25/17.
 */

interface LastFmEndpoint {

    @GET("2.0/")
    Observable<LastFmSearchResult> searchArtist(@Query("method") final String query, @Query("artist") final String name);

    @GET("2.0/")
    Observable<LastFmSearchResult> searchAlbum(@Query("method") final String query, @Query("album") final String name);

}
