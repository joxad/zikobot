package com.startogamu.musicalarm.module.component;

import android.content.Context;

import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.module.content_resolver.ContentResolverModule;
import com.startogamu.musicalarm.module.music.PlayerModule;
import com.startogamu.musicalarm.module.spotify_api.SpotifyApiModule;
import com.startogamu.musicalarm.module.spotify_auth.SpotifyAuthModule;

public enum Injector {

    INSTANCE;
    private SpotifyApiComponent spotifyApiComponent;
    private SpotifyAuthComponent spotifyAuthComponent;
    private ContentResolverComponent contentResolverComponent;
    private PlayerComponent playerComponent;

    public void initSpotifyApi(Context context) {
        //   spotifyApiComponent = DaggerSpotifyComponent
        spotifyApiComponent = DaggerSpotifyApiComponent.builder().spotifyApiModule(new SpotifyApiModule(context)).build();
    }

    public void initSpotifyAuth(Context context) {
        spotifyAuthComponent = DaggerSpotifyAuthComponent.builder().spotifyAuthModule(new SpotifyAuthModule(context)).build();
    }

    public void initContentResolver(Context context) {
        contentResolverComponent = DaggerContentResolverComponent.builder().contentResolverModule(new ContentResolverModule(context)).build();
    }

    public void initPlayerMusic(Context context) {
        playerComponent = DaggerPlayerComponent.builder().playerModule(new PlayerModule(context)).build();

    }

    public SpotifyAuthComponent spotifyAuth() {
        return spotifyAuthComponent;
    }

    public ContentResolverComponent contentResolverComponent() {
        return contentResolverComponent;
    }

    public SpotifyApiComponent spotifyApi() {
        return spotifyApiComponent;
    }

    public PlayerComponent playerComponent() {
        return playerComponent;
    }

}
