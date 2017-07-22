package com.joxad.zikobot.app.alarm;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.joxad.easydatabinding.bottomsheet.DialogBottomSheetBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.alarm.event.EventAlarmSelect;
import com.joxad.zikobot.app.core.fragmentmanager.IntentManager;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.databinding.DialogFragmentSettingsBinding;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.app.player.event.EventAddTrackToCurrent;
import com.joxad.zikobot.app.player.event.EventAddTrackToEndOfCurrent;
import com.joxad.zikobot.data.model.Alarm;
import com.joxad.zikobot.data.model.Album;
import com.joxad.zikobot.data.model.Artist;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;
import com.joxad.zikobot.data.module.soundcloud.manager.SoundCloudApiManager;
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager;
import com.joxad.zikobot.data.module.spotify_api.model.Item;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyTrack;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

import static com.joxad.zikobot.data.model.TYPE.LOCAL;
import static com.joxad.zikobot.data.model.TYPE.SOUNDCLOUD;
import static com.joxad.zikobot.data.model.TYPE.SPOTIFY;

/**
 * Created by josh on 09/03/16.
 */
public class DialogFragmentSettingsVM extends DialogBottomSheetBaseVM<DialogFragmentSettings, DialogFragmentSettingsBinding> {
    private static final String TAG = DialogFragmentSettingsVM.class.getSimpleName();
    public ObservableArrayList<AlarmVM> itemsVM;

    public ItemBinding itemView = ItemBinding.of(BR.itemAlarmVM, R.layout.item_alarm_dialog);

    @Nullable
    Track track;

    private Album album;
    private ArrayList<Track> tracks;
    private Item playlist;

    /***
     * @param fragment
     * @param binding
     */
    public DialogFragmentSettingsVM(DialogFragmentSettings fragment, DialogFragmentSettingsBinding binding) {
        super(fragment, binding);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstance) {
//unused
    }


    @Override
    public void onCreate() {
        if (fragment.getArguments().containsKey(EXTRA.TRACK)) {
            Parcelable parcelable = fragment.getArguments().getParcelable(EXTRA.TRACK);
            track = (parcelable != null ? Parcels.unwrap(parcelable) : null);
        }
        if (fragment.getArguments().containsKey(EXTRA.LOCAL_ALBUM)) {
            Parcelable parcelableAlbum = fragment.getArguments().getParcelable(EXTRA.LOCAL_ALBUM);
            album = (parcelableAlbum != null ? Parcels.unwrap(parcelableAlbum) : null);
            tracks = new ArrayList<>();
            switch (album.getType()) {
                case LOCAL:
                    loadLocalTracks();
                    break;
                case SPOTIFY:
                    loadSpotifyTracks();
                    break;
            }

        }

        if (fragment.getArguments().containsKey(EXTRA.PLAYLIST)) {
            Parcelable parcelablePlaylist = fragment.getArguments().getParcelable(EXTRA.PLAYLIST);
            playlist = (parcelablePlaylist != null ? Parcels.unwrap(parcelablePlaylist) : null);
            tracks = new ArrayList<>();
           loadSpotifyTracks();
        }
        itemsVM = new ObservableArrayList<>();
        notifyChange();
        loadAlarms();
    }


    private void loadSpotifyTracks() {

        SpotifyApiManager.getInstance().getAlbumTracks(album.getId()).subscribe(spotifyResultAlbum -> {
            for (SpotifyTrack track : spotifyResultAlbum.getTracks()) {
                tracks.add(Track.from(track, album.getImage()));
            }
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });
    }

    private void loadLocalTracks() {
        LocalMusicManager.getInstance().getLocalTracks(100, 0, null, Long.parseLong(album.getId()), null).subscribe(localTracks -> {
            for (LocalTrack localTrack : localTracks) {
                tracks.add(Track.from(localTrack));
            }
        }, throwable -> {
            Logger.d(TAG, throwable.getLocalizedMessage());
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(priority = 1)
    public void onEvent(EventAlarmSelect eventAlarmSelect) {
        //TODO
        EventBus.getDefault().cancelEventDelivery(eventAlarmSelect);

        Alarm alarm = eventAlarmSelect.getModel();
        AlarmTrackManager.clear();
        if (track != null)
            AlarmTrackManager.selectTrack(track);
        if (album != null) {
            for (Track track : tracks) {
                AlarmTrackManager.selectTrack(track);
            }
        }
        if (playlist != null) {
            for (Track track : tracks) {
                AlarmTrackManager.selectTrack(track);
            }
        }
        ArrayList<Track> allTracks = new ArrayList<>();
        allTracks.addAll(alarm.getTracks());
        allTracks.addAll(AlarmTrackManager.tracks());
        AlarmManager.saveAlarm(alarm, allTracks).subscribe(alarm1 -> {
            if (track != null)
                Toast.makeText(fragment.getContext(), track.getName() + " a été ajoutée à la playlist " + alarm.getName(), Toast.LENGTH_SHORT).show();
            if (album != null)
                Toast.makeText(fragment.getContext(), album.getName() + " a été ajouté à la playlist " + alarm.getName(), Toast.LENGTH_SHORT).show();
            if (playlist != null)
                Toast.makeText(fragment.getContext(), playlist.getName() + " a été ajouté à la playlist " + alarm.getName(), Toast.LENGTH_SHORT).show();
            dismiss();
        }, throwable -> {
            Toast.makeText(fragment.getContext(), "Oops j'ai eu un souci", Toast.LENGTH_SHORT).show();

        });

    }

    /**
     * add the current track to the current list direct next of playing track
     */
    public void addTrackToCurrent(@SuppressWarnings("unused") View view) {
        if (track != null)
            EventBus.getDefault().post(new EventAddTrackToCurrent(new TrackVM(fragment.getContext(), track)));
        if (album != null) {
            ObservableArrayList<TrackVM> trackVMs = new ObservableArrayList();
            for (Track track : tracks) {
                trackVMs.add(new TrackVM(fragment.getContext(), track));
            }
            EventBus.getDefault().post(new EventAddList(trackVMs));
        }
        if (playlist != null) {
            ObservableArrayList<TrackVM> trackVMs = new ObservableArrayList();
            for (Track track : tracks) {
                trackVMs.add(new TrackVM(fragment.getContext(), track));
            }
            EventBus.getDefault().post(new EventAddList(trackVMs));
        }
        dismiss();
    }

    /***
     * add the current track to the current list of playing track
     *
     * @param view
     */
    public void addTrackToEndOfCurrent(@SuppressWarnings("unused") View view) {
        if (track != null)
            EventBus.getDefault().post(new EventAddTrackToEndOfCurrent(new TrackVM(fragment.getContext(), track)));
        if (album != null) {
            ObservableArrayList<TrackVM> trackVMs = new ObservableArrayList();
            for (Track track : tracks) {
                trackVMs.add(new TrackVM(fragment.getContext(), track));
            }
            EventBus.getDefault().post(new EventAddList(trackVMs));
        }
        if (playlist != null) {
            ObservableArrayList<TrackVM> trackVMs = new ObservableArrayList();
            for (Track track : tracks) {
                trackVMs.add(new TrackVM(fragment.getContext(), track));
            }
            EventBus.getDefault().post(new EventAddList(trackVMs));
        }

        dismiss();
    }


    private void dismiss() {
        fragment.dismiss();
    }

    /***
     * @param view
     */
    public void showArtist(@SuppressWarnings("unused") View view) {
        if (track != null)
            switch (track.getType()) {
                case LOCAL:
                    showLocalArtist(track.getArtistName());
                    break;
                case SPOTIFY:
                    showSpotifyArtist(track.getArtistId());
                    break;
                case SOUNDCLOUD:
                    showSoundCloudArtist(track.getArtistId());
                    break;
            }
        if (album != null) {
            switch (album.getType()) {
                case LOCAL:
                    showLocalArtist(album.getArtist());
                    break;
                case SPOTIFY:
                    showSpotifyArtist(album.getArtistId());
                    break;
                case SOUNDCLOUD:
                    showSoundCloudArtist(album.getArtistId());
                    break;
            }
        }
        if (playlist != null) {
            Toast.makeText(fragment.getContext(), "Pas d'artiste ", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param artistId
     */
    private void showSoundCloudArtist(String artistId) {
        SoundCloudApiManager.getInstance().userById(Long.parseLong(artistId)).subscribe(soundCloudUser -> {
            fragment.startActivity(IntentManager.goToArtist(Artist.from(soundCloudUser)));
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });
    }

    private void showSpotifyArtist(String artistId) {
        SpotifyApiManager.getInstance().getArtist(artistId).subscribe(spotifyArtist -> {
            fragment.startActivity(IntentManager.goToArtist(Artist.from(spotifyArtist)));
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });
    }

    private void showLocalArtist(String artistName) {
        LocalMusicManager.getInstance().getLocalArtists(1, 0, artistName).subscribe(localArtists -> {
            fragment.startActivity(IntentManager.goToArtist(Artist.from(localArtists.get(0))));
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });
    }

    /**
     * Create a new alarm using the current track
     *
     * @param view
     */
    public void newAlarmClicked(@SuppressWarnings("unused") View view) {
        Alarm alarm = new Alarm();
        AlarmTrackManager.clear();
        if (track != null) {
            AlarmTrackManager.selectTrack(track);
            alarm.setName(track.getName());
        }
        if (album != null) {
            for (Track track : tracks) {
                AlarmTrackManager.selectTrack(track);
            }
            alarm.setName(album.getName());
        }
        AlarmManager.saveAlarm(alarm, AlarmTrackManager.tracks()).subscribe(alarm1 -> {
            String name = "";
            if (track != null)
                name = track.getName();
            if (album != null)
                name = album.getName();
            Toast.makeText(fragment.getContext(), name + " a été ajoutée à l'alarme " + alarm.getName(), Toast.LENGTH_SHORT).show();
            dismiss();
        }, throwable -> {
            Toast.makeText(fragment.getContext(), "Oops j'ai eu un souci", Toast.LENGTH_SHORT).show();

        });
    }

    /***
     *
     */
    public void loadAlarms() {
        AlarmManager.loadAlarms().subscribe(alarms -> {
            itemsVM.clear();
            for (Alarm alarm : alarms) {
                itemsVM.add(new AlarmVM(fragment.getContext(), alarm) {
                    @Override
                    public ItemBinding itemView() {
                        return null;
                    }
                });
            }


        }, throwable -> {

        });
    }

    public String getString(@StringRes int res) {
        return fragment.getString(res);
    }


    @Bindable
    public String getImage() {
        if (track != null) return track.getImageUrl();
        if (album != null) return album.getImage();
        if (playlist != null) return playlist.getImages().get(0).getUrl();
        return "";
    }

    @Bindable
    public String getTitle() {
        if (track != null) return track.getName();
        if (album != null) return album.getName();
        if (playlist != null) return playlist.getName();
        return "";
    }

    @Bindable
    public String getSubtitle() {
        if (track != null) return track.getArtistName();
        if (album != null) return album.getArtist();
        return "";
    }
}
