package com.joxad.zikobot.data.module.spotify_auth.resource;


import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyToken;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import io.reactivex.Observable;


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
            @Header("Content-Type") final String contentType,
            @Field("code") final String code,
            @Field("grant_type") final String grant_type,
            @Field("redirect_uri") final String redirectUri);


    @FormUrlEncoded
    @POST("/api/token")
    @Headers("Content-Type : application/x-www-form-urlencoded")
    Observable<SpotifyToken> refreshToken(
            @Field("refresh_token") final String code,
            @Field("grant_type") final String grant_type,
            @Field("redirect_uri") final String redirectUri);
}

