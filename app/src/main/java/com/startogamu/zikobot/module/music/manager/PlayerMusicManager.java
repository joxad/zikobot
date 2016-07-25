package com.startogamu.zikobot.module.music.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.deezer.sdk.model.PlayableEntity;
import com.deezer.sdk.player.PlayerWrapper;
import com.joxad.android_easy_spotify.SpotifyPlayerManager;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventShowMessage;
import com.startogamu.zikobot.core.event.TrackChangeEvent;
import com.startogamu.zikobot.core.event.player.DurationEvent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToCurrent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventNextPlayer;
import com.startogamu.zikobot.core.event.player.EventPausePlayer;
import com.startogamu.zikobot.core.event.player.EventPlayTrack;
import com.startogamu.zikobot.core.event.player.EventPreviousPlayer;
import com.startogamu.zikobot.core.event.player.EventResumePlayer;
import com.startogamu.zikobot.core.event.player.EventStopPlayer;
import com.startogamu.zikobot.core.notification.PlayerNotification;
import com.startogamu.zikobot.core.service.MediaPlayerService;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.deezer.manager.DeezerManager;
import com.startogamu.zikobot.module.deezer.manager.SimplifiedPlayerListener;
import com.startogamu.zikobot.module.zikobot.model.Alarm;
import com.startogamu.zikobot.module.zikobot.model.TYPE;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.viewmodel.base.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Singleton;

import lombok.Getter;

/**
 * {@link PlayerMusicManager} will handle the change of track according to the type of alarm track that is used
 * If a spotify track is finished and the next one is a local one, it is his job to deal with it
 */
@Singleton
public class PlayerMusicManager {

    private static final String TAG = PlayerMusicManager.class.getSimpleName();
    private final Context context;
    private Alarm alarm = null;
    @Getter
    int currentSong = 0;
    int currentType = -1;
    int currentPosition;
    private ArrayList<TrackVM> tracks = new ArrayList<>();
    private MediaPlayerService mediaPlayerService;
    private boolean mediaPlayerServiceBound = false;
    //connect to the service
    private Intent playIntent;
    private boolean isPlaying = false;
    private PlayerWrapper deezerPlayer;
    private MediaObserver observer;

    /***
     * @param context
     */
    public PlayerMusicManager(Context context) {
        this.context = context;
        initMediaPlayer(context);
        initSpotifyPlayer(context);
        initDeezerPlayer(context);
        observer = new MediaObserver();
        new Thread(observer).start();

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
                if (currentType == TYPE.SPOTIFY)
                    currentPosition = playerState.positionInMs;
                if (playerState.positionInMs >= playerState.durationInMs && playerState.durationInMs > 0 && pauseToHandle)
                    next();
                Log.d(TAG, String.format("Player state %s - activeDevice %s : current duration %d total duration %s", playerState.trackUri, playerState.activeDevice, playerState.positionInMs, playerState.durationInMs));
            }

            @Override
            public void onPlaybackError(ErrorType errorType, String s) {

            }
        }, null);
    }

    private void initDeezerPlayer(Context context) {
        DeezerManager.player().addPlayerListener(new SimplifiedPlayerListener() {
            @Override
            public void onTrackEnded(PlayableEntity playableEntity) {
                next();
            }
        });

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
        Track model = track.getModel();
        currentType = model.getType();
        switch (model.getType()) {
            case TYPE.LOCAL:
                pauseToHandle = false;
                SpotifyPlayerManager.pause();
                SpotifyPlayerManager.clear();
                handler.postDelayed(() -> pauseToHandle = true, 500);
                mediaPlayerService.playSong(Uri.parse(model.getRef()));
                break;
            case TYPE.SPOTIFY:
                if (mediaPlayerService != null)
                    mediaPlayerService.stop();
                SpotifyPlayerManager.play(model.getRef());
                break;
            case TYPE.SOUNDCLOUD:
                SpotifyPlayerManager.pause();
                SpotifyPlayerManager.clear();
                mediaPlayerService.playUrlSong(model.getRef());
                break;
            case TYPE.DEEZER:
                SpotifyPlayerManager.pause();
                SpotifyPlayerManager.clear();
                mediaPlayerService.pause();
                DeezerManager.playTrack(Long.parseLong(model.getRef()));
                break;
        }
        isPlaying = true;
        observer.resume();
        EventBus.getDefault().post(new TrackChangeEvent(model));
        PlayerNotification.show(model);

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
        observer.stop();
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

    /***
     * move position
     *
     * @param progress
     */
    public void seek(int progress) {
        switch (currentType) {
            case TYPE.LOCAL:
                mediaPlayerService.seek(progress);
                break;
            case TYPE.SPOTIFY:
                SpotifyPlayerManager.player().seekToPosition(progress);
                break;
            case TYPE.SOUNDCLOUD:
                mediaPlayerService.seek(progress);
                break;
            case TYPE.DEEZER:
                deezerPlayer.seek(currentPosition);
                break;
        }
    }


    private class MediaObserver implements Runnable {
        private AtomicBoolean stop = new AtomicBoolean(false);

        public void stop() {
            stop.set(true);
        }

        public void resume() {
            stop.set(false);
        }

        @Override
        public void run() {
            while (!stop.get()) {
                try {
                    if (isPlaying()) {
                        switch (currentType) {
                            case TYPE.LOCAL:
                                currentPosition = mediaPlayerService.getCurrentPosition();
                                break;
                            case TYPE.SPOTIFY:
                                //already done in playbackevent
                                break;
                            case TYPE.SOUNDCLOUD:
                                currentPosition = mediaPlayerService.getCurrentPosition();
                                break;
                            case TYPE.DEEZER:
                                currentPosition = (int) deezerPlayer.getPosition();
                                break;
                        }
                        sendMsgToUI(currentPosition);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //handles all the background threads things for you
    private void sendMsgToUI(int position) {
        DurationEvent event = new DurationEvent(position);
        EventBus.getDefault().post(event);
    }
}
