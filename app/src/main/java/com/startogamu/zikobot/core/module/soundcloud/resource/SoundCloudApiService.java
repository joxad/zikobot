package com.startogamu.zikobot.core.module.soundcloud.resource;

import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudToken;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudTrack;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudUser;

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
public interface SoundCloudApiService {

    @GET("users/{id}/playlists")
    Observable<ArrayList<SoundCloudPlaylist>> userPlaylists(@Path("id") final long userId);

    @GET("users/{id}/tracks")
    Observable<ArrayList<SoundCloudTrack>> userTracks(@Path("id") final long userId);

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
