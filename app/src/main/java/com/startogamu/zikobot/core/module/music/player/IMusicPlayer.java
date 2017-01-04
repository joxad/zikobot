package com.startogamu.zikobot.core.module.music.player;

/**
 * Created by josh on 28/08/16.
 */
public interface IMusicPlayer {
    void init();
    void play(String ref);
    void pause();
    void resume();
    void stop();
    void seekTo(int position);
    int position();
    int positionMax();
    void next();
    void previous();
}
