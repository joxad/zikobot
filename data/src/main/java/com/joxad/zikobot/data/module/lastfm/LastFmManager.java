package com.joxad.zikobot.data.module.lastfm;

import android.content.Context;

import com.joxad.zikobot.data.R;
import com.joxad.zikobot.data.module.lastfm.model.Album;
import com.joxad.zikobot.data.module.lastfm.model.Artist;
import com.joxad.zikobot.data.ws.RetrofitBase;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.Request;

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


    public Observable<List<Artist>> searchArtist(final String type, final String name) {
        return endpoint.searchArtist(type, name).flatMap(lastFmSearchResult -> Observable.just(lastFmSearchResult.result.getArtistmatches().getArtist()));
    }

    public Observable<List<Album>> searchAlbum(final String type, final String name) {
        return endpoint.searchAlbum(type, name).flatMap(lastFmSearchResult -> Observable.just(lastFmSearchResult.result.getAlbummatches().getAlbum()));
    }

    public Observable<Artist> findArtist(final String name) {
        return searchArtist("artist.search", name).flatMap(results -> {
            if (!results.isEmpty())
                return Observable.just(results.get(0));
            else
                return Observable.just(null);
        });
    }

    public Observable<Album> findAlbum(final String name) {
        return searchAlbum("album.search", name).flatMap(results -> {
            if (!results.isEmpty())
                return Observable.just(results.get(0));
            else
                return Observable.just(null);
        });
    }
}
