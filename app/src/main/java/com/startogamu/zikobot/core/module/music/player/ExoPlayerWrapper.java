package com.startogamu.zikobot.core.module.music.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.orhanobut.logger.Logger;

import java.io.IOException;

/**
 * Created by Jocelyn on 08/12/2016.
 */

public class ExoPlayerWrapper implements IMusicPlayer, ExtractorMediaSource.EventListener {

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 256;
    private Context context;
    SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private android.os.Handler mainHandler = new Handler();
    private DataSource.Factory mediaDataSourceFactory;

    public ExoPlayerWrapper(Context context) {
        this.context = context;
        init();
    }

    @Override
    public void init() {
        initializePlayer();

    }

    private void initializePlayer() {
        if (player == null) {

            mediaDataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "ExoPlayerDemo"));
            trackSelector = new DefaultTrackSelector(mainHandler);
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, new DefaultLoadControl());

            player.setPlayWhenReady(true);
        }


    }


    public void play(Uri uri) {
        MediaSource mediaSource = new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                mainHandler, this);
        player.prepare(mediaSource);
    }

    @Override
    public void play(String ref) {
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(ref), mediaDataSourceFactory, new DefaultExtractorsFactory(),
                mainHandler, this);
        player.prepare(mediaSource);
    }

    @Override
    public void pause() {
        player.stop();
    }

    @Override
    public void resume() {
    }

    @Override
    public void stop() {

    }

    @Override
    public void seekTo(int position) {

    }

    @Override
    public float position() {
        return 0;
    }

    @Override
    public void next() {

    }

    @Override
    public void onLoadError(IOException error) {
        Logger.d(ExoPlayerWrapper.class.getSimpleName(), "" + error.getCause() + " - " + error.getLocalizedMessage());
    }
}
