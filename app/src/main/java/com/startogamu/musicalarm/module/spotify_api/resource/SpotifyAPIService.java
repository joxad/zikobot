package com.startogamu.musicalarm.module.spotify_api.resource;


import com.startogamu.musicalarm.module.spotify_api.object.SpotifyUser;

import retrofit2.http.GET;
import rx.Observable;


/**
 * Created by josh on 11/03/16.
 * <p>
 * Interface using retrofit to call Spotify API
 * </p>
 */
public interface SpotifyAPIService {

    @GET("me")
    Observable<SpotifyUser> getMe();
}
