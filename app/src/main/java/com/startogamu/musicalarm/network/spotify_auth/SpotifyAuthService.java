package com.startogamu.musicalarm.network.spotify_auth;


import com.startogamu.musicalarm.model.spotify.SpotifyToken;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Methods used by the Auth Api of Spotify
 */
public interface SpotifyAuthService {

    @FormUrlEncoded
    @POST("/api/token")
    @Headers("Content-Type : application/x-www-form-urlencoded")
    Observable<SpotifyToken> requestToken(@Header("Authorization") final String authorization,
                                          @Field("code") final String code,
                                          @Field("grant_type") final String grant_type,
                                          @Field("redirect_uri") final String reirectUri);
}
