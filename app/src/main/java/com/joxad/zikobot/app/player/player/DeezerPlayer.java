package com.joxad.zikobot.app.player.player;

import android.app.Application;
import android.content.Context;

import com.deezer.sdk.model.PlayableEntity;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.player.TrackPlayer;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;
import com.joxad.zikobot.data.R;
import com.joxad.zikobot.app.player.event.EventNextTrack;
import com.orhanobut.logger.Logger;
import com.joxad.zikobot.data.module.deezer.SimplifiedPlayerListener;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 28/08/16.
 */
public class DeezerPlayer implements IMusicPlayer {


    private TrackPlayer trackPlayer;
    private Context context;

    public DeezerPlayer(Context context) {
        this.context = context;
        init();
    }


    @Override
    public void init() {
        DeezerConnect deezerConnect = DeezerConnect.forApp(context.getString(R.string.deezer_id)).build();
        try {
            trackPlayer = new TrackPlayer((Application) context.getApplicationContext(), deezerConnect, new WifiAndMobileNetworkStateChecker());

        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        }

        trackPlayer.addOnPlayerStateChangeListener((playerState, l) -> Logger.d("Player state %s" ,playerState.name()));
        trackPlayer.addOnPlayerErrorListener((e, l) -> {
            Logger.e(e.getLocalizedMessage());
        });

        trackPlayer.addPlayerListener(new SimplifiedPlayerListener() {
            @Override
            public void onTrackEnded(PlayableEntity playableEntity) {
              //  next();
            }
        });
    }

    @Override
    public void play(String ref) {
        trackPlayer.playTrack(Long.parseLong(ref));
    }

    @Override
    public void pause() {
        trackPlayer.pause();
    }

    @Override
    public void resume() {
        trackPlayer.play();
    }

    @Override
    public void stop() {
        //trackPlayer.stop();
    }

    @Override
    public void seekTo(int position) {
        trackPlayer.seek(position);
    }

    @Override
    public int position() {
        return (int)trackPlayer.getPosition();
    }

    @Override
    public int positionMax() {
        return 0;
    }


    @Override
    public void next() {
        EventBus.getDefault().post(new EventNextTrack());
    }

    @Override
    public void previous() {

    }
}
