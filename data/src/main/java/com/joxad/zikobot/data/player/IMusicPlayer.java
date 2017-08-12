package com.joxad.zikobot.data.player;

/**
 * Created by josh on 28/08/16.
 */
public interface IMusicPlayer {
    void init();

    void play(String ref);

    void pause();

    void resume();

    void stop();

    void seekTo(float percent);

    void seekTo(int position);

}