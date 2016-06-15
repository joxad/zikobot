package com.startogamu.zikobot.module.component;

import android.content.Context;

import com.startogamu.zikobot.module.content_resolver.ContentResolverModule;
import com.startogamu.zikobot.module.music.PlayerModule;
import com.startogamu.zikobot.module.soundcloud.SoundCloudApiBaseComponent;
import com.startogamu.zikobot.module.soundcloud.SoundCloudApiModule;
import com.startogamu.zikobot.module.spotify_api.SpotifyApiModule;
import com.startogamu.zikobot.module.spotify_auth.SpotifyAuthModule;

public enum Injector {

    INSTANCE;
    private SpotifyApiComponent spotifyApiComponent;
    private SpotifyAuthComponent spotifyAuthComponent;
    private ContentResolverComponent contentResolverComponent;
    private PlayerComponent playerComponent;
    private SoundCloudApiComponent soundCloudApiComponent;

    public void init(Context context){
        initSpotifyApi(context);
        initSpotifyAuth(context);
        initContentResolver(context);
        initPlayerMusic(context);
        initSoundCloudApi(context);
    }
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

    public void initSoundCloudApi(Context context) {
        soundCloudApiComponent = DaggerSoundCloudApiComponent.builder().soundCloudApiModule(new SoundCloudApiModule(context)).build();
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

    public SoundCloudApiComponent soundCloudApi() {
        return soundCloudApiComponent;
    }
}
