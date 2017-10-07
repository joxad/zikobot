package com.joxad.zikobot.data.module.spotify_auth.resource;


import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyToken;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * @author Fabien Baron
 * @company Phoceis
 * @email fbaron@phoceis.com
 * @date 11/04/2016
 */
public interface SpotifyAuthService {
    @FormUrlEncoded
    @POST("token")
    Observable<SpotifyToken> requestToken(
            @Field("code") final String code,
            @Field("grant_type") final String grant_type,
            @Field("redirect_uri") final String redirectUri);


    @FormUrlEncoded
    @POST("/api/token")
    Single<SpotifyToken> refreshToken(
            @Field("refresh_token") final String code,
            @Field("grant_type") final String grant_type);
}

