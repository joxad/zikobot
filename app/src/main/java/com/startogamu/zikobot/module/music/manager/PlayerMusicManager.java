package com.startogamu.zikobot.module.music.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.joxad.android_easy_spotify.SpotifyPlayerManager;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventShowMessage;
import com.startogamu.zikobot.core.event.TrackChangeEvent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToCurrent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventNextPlayer;
import com.startogamu.zikobot.core.event.player.EventPausePlayer;
import com.startogamu.zikobot.core.event.player.EventPlayTrack;
import com.startogamu.zikobot.core.event.player.EventPreviousPlayer;
import com.startogamu.zikobot.core.event.player.EventResumePlayer;
import com.startogamu.zikobot.core.event.player.EventStopPlayer;
import com.startogamu.zikobot.core.notification.PlayerNotification;
import com.startogamu.zikobot.core.receiver.ClearPlayerReceiver;
import com.startogamu.zikobot.core.service.MediaPlayerService;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.zikobot.model.Alarm;
import com.startogamu.zikobot.module.zikobot.model.TYPE;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.viewmodel.base.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Singleton;

/**
 * {@link PlayerMusicManager} will handle the change of track according to the type of alarm track that is used
 * If a spotify track is finished and the next one is a local one, it is his job to deal with it
 */
@Singleton
public class PlayerMusicManager {

    private static final String TAG = PlayerMusicManager.class.getSimpleName();
    private final Context context;
    private Alarm alarm = null;
    int currentSong = 0;
    private ArrayList<TrackVM> tracks = new ArrayList<>();
    private MediaPlayerService mediaPlayerService;
    private boolean mediaPlayerServiceBound = false;
    //connect to the service
    private Intent playIntent;
    private boolean isPlaying = false;

    /***
     * @param context
     */
    public PlayerMusicManager(Context context) {
        this.context = context;
        initMediaPlayer(context);
        initSpotifyPlayer(context);
        EventBus.getDefault().register(this);
    }

    /***
     * Method called when the token is updated for spotify
     */
    public void refreshAccessTokenPlayer() {
        SpotifyPlayerManager.updateToken(AppPrefs.getSpotifyAccessToken());
    }

    /***
     * service connexion that will handle the binding with the {@link MediaPlayerService} service
     */
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MediaPlayerServiceBinder binder = (MediaPlayerService.MediaPlayerServiceBinder) service;
            //get service
            mediaPlayerService = binder.getService();
            mediaPlayerService.setOnCompletionListener(mp -> {
                next();
            });
            //pass list
            mediaPlayerServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mediaPlayerServiceBound = false;
        }
    };

    /***
     * @param context
     */
    public void initMediaPlayer(Context context) {
        if (playIntent == null) {
            playIntent = new Intent(context, MediaPlayerService.class);
            context.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            context.startService(playIntent);
        }
    }

    /***
     * Initialize the SpotifyPlayer according to the data found with accesstoken
     *
     * @param context
     */
    private void initSpotifyPlayer(Context context) {

        SpotifyPlayerManager.startPlayer(context, AppPrefs.getSpotifyAccessToken(), new Player.InitializationObserver() {
            @Override
            public void onInitialized(Player player) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, new PlayerNotificationCallback() {
            @Override
            public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
                if (eventType == EventType.PAUSE) {
                    Log.d(TAG, "PAUSE");
                }
                if (eventType == EventType.PLAY) {
                    Log.d(TAG, "PLAY");
                }
                if (eventType == EventType.TRACK_CHANGED) {
                    Log.d(TAG, "TRACK_CHANGED");
                }
                if (eventType == EventType.SKIP_NEXT) {
                    Log.d(TAG, "SKIP_NEXT");
                }
                if (eventType == EventType.SKIP_PREV) {
                    Log.d(TAG, "SKIP_PREV");
                }
                if (eventType == EventType.TRACK_END) {
                    Log.d(TAG, "TRACK END");

                }
                if (playerState.positionInMs >= playerState.durationInMs && playerState.durationInMs > 0 && pauseToHandle)
                    next();
                Log.d(TAG, String.format("Player state %s - activeDevice %s : current duration %d total duration %s", playerState.trackUri, playerState.activeDevice, playerState.positionInMs, playerState.durationInMs));
            }

            @Override
            public void onPlaybackError(ErrorType errorType, String s) {

            }
        }, null);
    }


    Handler handler = new Handler();
    private boolean pauseToHandle = true;

    boolean newTrack = true;

    /***
     * @param track
     */
    private void playTrack(final TrackVM track) {
        int i = 0;
        newTrack = true;
        for (TrackVM t : tracks) {
            if (t.getModel().getRef().equals(track.getModel().getRef())) {
                currentSong = i;
                newTrack = false;
                break;
            }
            i++;
        }
        if (newTrack) {
            currentSong = 0;
            tracks.clear();
            tracks.add(track);
        }
        switch (track.getModel().getType()) {
            case TYPE.LOCAL:
                pauseToHandle = false;
                SpotifyPlayerManager.pause();
                SpotifyPlayerManager.clear();
                handler.postDelayed(() -> pauseToHandle = true, 500);
                mediaPlayerService.playSong(Uri.parse(track.getModel().getRef()));
                break;
            case TYPE.SPOTIFY:
                mediaPlayerService.stop();
                SpotifyPlayerManager.play(track.getModel().getRef());
                break;
            case TYPE.SOUNDCLOUD:
                SpotifyPlayerManager.pause();
                SpotifyPlayerManager.clear();
                mediaPlayerService.playUrlSong(track.getModel().getRef());
                break;
        }
        isPlaying = true;
        EventBus.getDefault().post(new TrackChangeEvent(track.getModel()));
        PlayerNotification.show(track.getModel());

    }

    /***
     *
     */
    public void previous() {
        currentSong--;
        if (currentSong >= 0)
            playTrack(tracks.get(currentSong));
    }

    /***
     *
     */
    public void next() {
        currentSong++;
        if (tracks.size() > currentSong) {
            playTrack(tracks.get(currentSong));
        } else {
            stop();
        }
    }


    /***
     * @param
     */
    public void playTracks() {
        currentSong = 0;
        playTrack(tracks.get(currentSong));

    }

    /***
     * @param alarm
     */
    public void startAlarm(Alarm alarm) {
        currentSong = 0;
        this.alarm = alarm;
        this.tracks.clear();
        for (Track track : alarm.getTracks()) {
            this.tracks.add(new TrackVM(context, track));
        }
        if (alarm.getRandomTrack() == 1) {
            Collections.shuffle(tracks);
        }
        playTrack(tracks.get(currentSong));
    }

    /***
     * Stop all the players
     */
    public void stop() {
        SpotifyPlayerManager.pause();
        mediaPlayerService.stop();
        currentSong = 0;

    }

    /***
     *
     */
    public void resume() {
        mediaPlayerService.resume();
        SpotifyPlayerManager.resume();
        PlayerNotification.updatePlayStatus(false);
    }

    /**
     * pause all the players
     */
    public void pause() {
        mediaPlayerService.pause();
        SpotifyPlayerManager.pause();
        PlayerNotification.updatePlayStatus(true);
    }

    public int getCurrentSong() {
        return currentSong;
    }

    public ArrayList<TrackVM> trackVMs() {
        return tracks;
    }


    @Subscribe
    public void onReceive(EventPlayTrack eventPlayTrack) {
        if (trackIsPlayable(eventPlayTrack.getTrack().getModel())) {
            playTrack(eventPlayTrack.getTrack());
        }
    }

    private boolean trackIsPlayable(Track model) {
        if (model.getType() == TYPE.SPOTIFY) {
            if (!AppPrefs.spotifyUser().getProduct().equalsIgnoreCase("premium")) {
                EventBus.getDefault().post(new EventShowMessage(context.getString(R.string.oops), context.getString(R.string.no_premium)));
                return false;
            }
        }
        if (model.getRef().contains(".m4a")) {
            EventBus.getDefault().post(new EventShowMessage(context.getString(R.string.oops), context.getString(R.string.no_m4a_support)));
            return false;
        }
        return true;
    }


    @Subscribe
    public void onReceive(EventAddTrackToPlayer eventAddTrackToPlayer) {
        tracks.clear();
        tracks.addAll(eventAddTrackToPlayer.getItems());
        for (int i = tracks.size() - 1; i >= 0; i--) {
            if (!trackIsPlayable(tracks.get(i).getModel())) {
                tracks.remove(i);
            }
        }
        isPlaying = true;
        playTracks();
    }


    @Subscribe
    public void onReceive(EventAddTrackToCurrent eventAddTrackToPlayer) {
        tracks.add(eventAddTrackToPlayer.getTrackVM());
    }

    @Subscribe
    public void onReceive(EventPreviousPlayer eventPreviousPlayer) {
        previous();
    }

    @Subscribe
    public void onReceive(EventNextPlayer eventResumePlayer) {
        next();
    }

    @Subscribe
    public void onReceive(EventResumePlayer eventResumePlayer) {
        resume();
    }

    @Subscribe
    public void onReceive(EventPausePlayer eventPausePlayer) {
        pause();
    }

    @Subscribe
    public void onReceive(EventStopPlayer eventStopPlayer) {
        tracks.clear();
        trackVMs().clear();
        PlayerNotification.cancel();
        stop();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void playOrResume() {
        isPlaying = !isPlaying;
        if (isPlaying) {
            Injector.INSTANCE.playerComponent().manager().resume();
        } else {
            Injector.INSTANCE.playerComponent().manager().pause();
        }
    }

    public void playUrl(String s) {
        mediaPlayerService.playUrlSong(s);
    }
}
