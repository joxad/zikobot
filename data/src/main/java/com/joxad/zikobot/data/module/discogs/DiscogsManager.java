package com.joxad.zikobot.data.module.discogs;

import android.content.Context;

import com.joxad.zikobot.data.R;
import com.joxad.zikobot.data.module.discogs.model.DiscogsResult;
import com.joxad.zikobot.data.ws.RetrofitBase;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;
import rx.Observable;

/**
 * Created by linux on 7/25/17.
 */
public enum DiscogsManager {
    INSTANCE;

    private DiscogsEndpoint endpoint;

    public void init(Context context) {
        endpoint = new RetrofitBase(context.getString(R.string.discogs_base_url), chain -> {
            Request request = chain.request();
            HttpUrl originalHttpUrl = request.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("key", context.getString(R.string.discogs_key) )
                    .addQueryParameter("secret", context.getString(R.string.discogs_secret) )
                    .build();

            return chain.proceed(request.newBuilder().url(url).build());
        }).create(DiscogsEndpoint.class);
    }


    public Observable<List<DiscogsResult>> search(final String query, final String type) {
        return endpoint.search(query, type).flatMap(discogsBaseSearchResult -> Observable.just(discogsBaseSearchResult.getResults()));
    }

    public Observable<DiscogsResult> findArtist(final String name) {
        return search(name, "artist").flatMap(discogsResults -> {
            if (!discogsResults.isEmpty())
                return Observable.just(discogsResults.get(0));
            else
                return Observable.just(null);
        });
    }
}
