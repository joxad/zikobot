package com.startogamu.zikobot.module.soundcloud.resource;

import com.startogamu.zikobot.module.soundcloud.model.SoundCloudTrack;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyPlaylist;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by josh on 15/06/16.
 */
public interface SoundCloudApiService {


    @GET("tracks/13158665.json")
    Observable<SoundCloudTrack> getTrack();

}
