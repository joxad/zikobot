package com.startogamu.zikobot.module.music.manager;

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
    float position();
    void next();
}
