package com.startogamu.zikobot.core.module.music;

import android.content.ContentUris;
import android.content.Context;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventShowMessage;
import com.startogamu.zikobot.core.event.player.EventAddTrackToCurrent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventNextTrack;
import com.startogamu.zikobot.core.event.player.EventPlayTrack;
import com.startogamu.zikobot.core.event.player.EventStopPlayer;
import com.startogamu.zikobot.core.event.player.TrackChangeEvent;
import com.startogamu.zikobot.core.module.localmusic.model.LocalTrack;
import com.startogamu.zikobot.core.module.music.player.ExoPlayerWrapper;
import com.startogamu.zikobot.core.notification.PlayerNotification;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.core.module.music.player.AndroidPlayer;
import com.startogamu.zikobot.core.module.music.player.DeezerPlayer;
import com.startogamu.zikobot.core.module.music.listener.DurationListener;
import com.startogamu.zikobot.core.module.music.player.IMusicPlayer;
import com.startogamu.zikobot.core.module.music.listener.PlayerStatusListener;
import com.startogamu.zikobot.core.module.music.player.SpotifyPlayer;
import com.startogamu.zikobot.core.module.music.player.VLCPlayer;
import com.startogamu.zikobot.core.model.Alarm;
import com.startogamu.zikobot.core.model.TYPE;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.localtracks.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link PlayerMusicManager} will handle the change of track according to the type of alarm track that is used
 * If a spotify track is finished and the next one is a local one, it is his job to deal with it
 */
public class PlayerMusicManager {

    private static final String TAG = PlayerMusicManager.class.getSimpleName();
    private Context context;

    private ArrayList<TrackVM> tracks = new ArrayList<>();

    @Setter
    private DurationListener durationListener;

    @Setter
    private PlayerStatusListener playerStatusListener;

    @Getter
    int currentSong = 0;
    int currentType = -1;
    public boolean isPlaying = false;
    private Handler trackPositionHandler;
    private IMusicPlayer currentPlayer;
    private PlayerNotification playerNotification;
    private VLCPlayer vlcPlayer;
    private DeezerPlayer deezerPlayer;
    private SpotifyPlayer spotifyPlayer;
    private AndroidPlayer androidPlayer;
    private ExoPlayerWrapper exoPlayerWrapper;

    /**
     * Constructeur privé
     */
    private PlayerMusicManager() {
    }


    /**
     * Constructeur privé
     */
    public void init(Context context) {
        this.context = context;
        EventBus.getDefault().register(this);
        androidPlayer = new AndroidPlayer(context);
        spotifyPlayer = new SpotifyPlayer(context);
        deezerPlayer = new DeezerPlayer(context);
        playerNotification = new PlayerNotification(context);
        vlcPlayer = new VLCPlayer(context);
        trackPositionHandler = new Handler();
        exoPlayerWrapper= new ExoPlayerWrapper(context);
    }

    /**
     * Holder
     */
    private static class PlayerMusicManagerHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static PlayerMusicManager instance = new PlayerMusicManager();
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static PlayerMusicManager getInstance() {
        return PlayerMusicManagerHolder.instance;
    }

    /***
     * @param track
     */
    private void playTrack(final TrackVM track) {
        checkIfTracksAlreadyHere(track);
        track.isPlaying.set(true);
        Track model = track.getModel();
        handlePlay(model);
        observe();
        EventBus.getDefault().post(new TrackChangeEvent(model));
        if (playerStatusListener != null) {
            playerStatusListener.onUpdate(isPlaying);
        }
        playerNotification.show(model);
    }

    /**
     * Verify if the track is already present in the playlist, if So play it
     * else add it as a new tracks
     *
     * @param track
     */
    private void checkIfTracksAlreadyHere(TrackVM track) {
        int i = 0;
        boolean newTrack = true;
        for (TrackVM t : tracks) {
            t.isPlaying.set(false);
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
    }

    /**
     * choose the player to use according to the type
     *
     * @param model
     */
    private void handlePlay(Track model) {
        if (currentPlayer != null)
            currentPlayer.pause();
        currentType = model.getType();
        switch (model.getType()) {
            case TYPE.LOCAL:
                currentPlayer = exoPlayerWrapper;
                break;
            case TYPE.SPOTIFY:
                currentPlayer = spotifyPlayer;
                break;
            case TYPE.SOUNDCLOUD:
                currentPlayer = androidPlayer;
                break;
            case TYPE.DEEZER:
                currentPlayer = deezerPlayer;
                break;
        }
        isPlaying = true;
        currentPlayer.play(model.getRef());
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
        getCurrentTrackVM().isPlaying.set(false);
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
        tracks.clear();
        Crashlytics.log("Start alarm " + alarm.getName() + " NbTracks : " + +alarm.getTracks().size());
        for (Track track : alarm.getTracks()) {
            tracks.add(new TrackVM(context, track));
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
        if (currentPlayer!=null) {
            currentPlayer.stop();
            currentSong = 0;
            playerNotification.cancel();
        }
    }

    /***
     *
     */
    public void resume() {
        getCurrentTrackVM().isPlaying.set(true);
        currentPlayer.resume();
        playerNotification.updatePlayStatus(false);
    }

    /**
     * pause all the players
     */
    public void pause() {
        getCurrentTrackVM().isPlaying.set(false);
        currentPlayer.pause();
        playerNotification.updatePlayStatus(true);
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

    @Subscribe
    public void onReceive(EventNextTrack ev) {
        next();
    }

    private boolean trackIsPlayable(Track model) {
        if (model.getType() == TYPE.SPOTIFY) {
            if (!AppPrefs.spotifyUser().getProduct().equalsIgnoreCase("premium")) {
                EventBus.getDefault().post(new EventShowMessage(context.getString(R.string.oops), context.getString(R.string.no_premium)));
                return false;
            }
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


    @Subscribe(priority = 10)
    public void onReceive(EventAddTrackToCurrent eventAddTrackToPlayer) {
        tracks.add(eventAddTrackToPlayer.getTrackVM());
    }


    @Subscribe
    public void onReceive(EventStopPlayer eventStopPlayer) {
        tracks.clear();
        trackVMs().clear();
        playerNotification.cancel();
        stop();
    }

    public void playOrResume() {
        isPlaying = !isPlaying;
        if (isPlaying) {
            resume();
        } else {
            pause();
        }
        if (playerStatusListener != null) {
            playerStatusListener.onUpdate(isPlaying);
        }
    }

    /***
     * move position
     *
     * @param progress
     */
    public void seek(int progress) {
        currentPlayer.seekTo(progress);
    }


    public TrackVM getCurrentTrackVM() {
        if (tracks.isEmpty()) {
            return new TrackVM(context, new Track());
        }
        return tracks.get(currentSong);
    }


    public void observe() {
        trackPositionHandler.postDelayed(() -> {
            if (isPlaying) {
                sendMsgToUI(currentPlayer.position());
            }
            observe();

        }, 200);
    }


    //handles all the background threads things for you
    private void sendMsgToUI(float position) {
        if (durationListener != null)
            durationListener.onUpdate(position);
    }
}
