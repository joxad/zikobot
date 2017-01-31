package com.joxad.zikobot.app.core.module.deezer;

import com.deezer.sdk.model.PlayableEntity;
import com.deezer.sdk.player.event.PlayerWrapperListener;
import com.orhanobut.logger.Logger;

/**
 * Created by josh on 25/07/16.
 */
public abstract class SimplifiedPlayerListener implements PlayerWrapperListener {
    @Override
    public void onAllTracksEnded() {
        Logger.d("End");
    }

    @Override
    public void onPlayTrack(PlayableEntity playableEntity) {
        Logger.d(playableEntity.toString());
    }

    @Override
    public abstract void onTrackEnded(PlayableEntity playableEntity) ;

    @Override
    public void onRequestException(Exception e, Object o) {
        Logger.e(e.getLocalizedMessage());
    }
}
