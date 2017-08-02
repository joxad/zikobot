package com.joxad.zikobot.data.module.lastfm;

import android.content.Context;

import com.joxad.zikobot.data.R;
import com.joxad.zikobot.data.module.lastfm.model.Artist;
import com.joxad.zikobot.data.ws.RetrofitBase;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;
import io.reactivex.Observable;

/**
 * Created by linux on 7/25/17.
 */
public enum LastFmManager {
    INSTANCE;

    private LastFmEndpoint endpoint;

    public void init(Context context) {
        endpoint = new RetrofitBase(context.getString(R.string.lastfm_base_url), chain -> {
            Request request = chain.request();
            HttpUrl originalHttpUrl = request.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", context.getString(R.string.lastfm_key))
                    .addQueryParameter("format", "json")
                    .build();

            return chain.proceed(request.newBuilder().url(url).build());
        }).create(LastFmEndpoint.class);
    }


    public Observable<List<Artist>> search(final String query, final String type) {
        return endpoint.search(query, type).flatMap(discogsBaseSearchResult -> Observable.just(discogsBaseSearchResult.result.getArtistmatches().getArtist()));
    }

    public Observable<Artist> findArtist(final String name) {
        return search("artist.search", name).flatMap(discogsResults -> {
            if (!discogsResults.isEmpty())
                return Observable.just(discogsResults.get(0));
            else
                return Observable.just(null);
        });
    }
}
