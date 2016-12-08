package com.startogamu.zikobot.core.module.music.player;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.Util;

/**
 * Created by Jocelyn on 08/12/2016.
 */

public class ExoPlayerWrapper  implements IMusicPlayer{

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 256;
    private Context context;
    private ExoPlayer player;
    private MediaCodecAudioTrackRenderer audioRenderer;
    private DefaultAllocator allocator;
    private DefaultUriDataSource dataSource;

    public ExoPlayerWrapper(Context context) {
        this.context = context;
        init();
    }
    @Override
    public void init() {

        player  = ExoPlayer.Factory.newInstance(1);
        allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
        String userAgent = Util.getUserAgent(context, "ExoPlayerDemo");
         dataSource = new DefaultUriDataSource(context, null, userAgent);



    }

    @Override
    public void play(String ref) {
        Uri radioUri = Uri.parse(ref);
// Settings for exoPlayer
        ExtractorSampleSource sampleSource = new ExtractorSampleSource(
                radioUri, dataSource, allocator, BUFFER_SEGMENT_SIZE * BUFFER_SEGMENT_COUNT);
        audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource);
// Prepare ExoPlayer
        player.prepare(audioRenderer);
        player.setPlayWhenReady(true);
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
}
