package com.joxad.zikobot.data.module.soundcloud.resource;

import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudToken;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudTrack;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudUser;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by josh on 15/06/16.
 */
public interface SoundCloudApiEndpoint {
    @GET("users/{id}")
    Observable<SoundCloudUser> user(@Path("id") final long userId);

    @GET("users/{id}/playlists")
    Observable<ArrayList<SoundCloudPlaylist>> userPlaylists(@Path("id") final long userId);

    @GET("users/{id}/tracks")
    Observable<ArrayList<SoundCloudTrack>> userTracks(@Path("id") final long userId);

    @GET("tracks")
    Observable<ArrayList<SoundCloudTrack>> search(@Query("q") final String searchFilter);

    @GET("playlists")
    Observable<ArrayList<SoundCloudPlaylist>> searchPlaylist(@Query("q") final String searchFilter);

    @GET("users")
    Observable<ArrayList<SoundCloudUser>> searchUser(@Query("q") final String searchFilter);

    @FormUrlEncoded
    @POST("oauth2/token")
    Observable<SoundCloudToken> token(@Field("client_id") final String clientId,
                                      @Field("client_secret") final String clientSecret,
                                      @Field("redirect_uri") final String redirectUri,
                                      @Field("code") final String code,
                                      @Field("grant_type") String authorization_code);

    @GET("me")
    Observable<SoundCloudUser> getMe();

}
