package com.startogamu.zikobot.alarm;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.joxad.easydatabinding.bottomsheet.DialogBottomSheetBaseVM;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.alarm.EventAlarmSelect;
import com.startogamu.zikobot.core.event.player.EventAddTrackToCurrent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToEndOfCurrent;
import com.startogamu.zikobot.core.model.Alarm;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.core.module.localmusic.manager.LocalMusicManager;
import com.startogamu.zikobot.core.module.localmusic.model.LocalAlbum;
import com.startogamu.zikobot.core.module.localmusic.model.LocalTrack;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.DialogFragmentSettingsBinding;
import com.startogamu.zikobot.localtracks.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 09/03/16.
 */
public class DialogFragmentSettingsVM extends DialogBottomSheetBaseVM<DialogFragmentSettings, DialogFragmentSettingsBinding> {
    private static final String TAG = DialogFragmentSettingsVM.class.getSimpleName();
    public ObservableArrayList<AlarmVM> itemsVM;

    public ItemView itemView = ItemView.of(BR.itemAlarmVM, R.layout.item_alarm_dialog);

    @Nullable
    Track track;

    private LocalAlbum album;
    private ArrayList<Track> tracks;

    /***
     * @param fragment
     * @param binding
     */
    public DialogFragmentSettingsVM(DialogFragmentSettings fragment, DialogFragmentSettingsBinding binding) {
        super(fragment, binding);
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
            LocalMusicManager.getInstance().getLocalTracks(100, 0, null, album.getId(), null).subscribe(localTracks -> {
                for (LocalTrack localTrack : localTracks) {
                    tracks.add(Track.from(localTrack));
                }
            }, throwable -> {
                Logger.d(TAG, throwable.getLocalizedMessage());
            });
        }
        itemsVM = new ObservableArrayList<>();
        notifyChange();
        loadAlarms();
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
        ArrayList<Track> allTracks = new ArrayList<>();
        allTracks.addAll(alarm.getTracks());
        allTracks.addAll(AlarmTrackManager.tracks());
        AlarmManager.saveAlarm(alarm, allTracks).subscribe(alarm1 -> {
            Toast.makeText(fragment.getContext(), track.getName() + " a été ajoutée à la playlist " + alarm.getName(), Toast.LENGTH_SHORT).show();
            dismiss();
        }, throwable -> {
            Toast.makeText(fragment.getContext(), "Oops j'ai eu un souci", Toast.LENGTH_SHORT).show();

        });

    }

    public void addTrackToCurrent(View view) {
        EventBus.getDefault().post(new EventAddTrackToCurrent(new TrackVM(fragment.getContext(), track)));
        dismiss();
    }

    public void addTrackToEndOfCurrent(View view) {
        EventBus.getDefault().post(new EventAddTrackToEndOfCurrent(new TrackVM(fragment.getContext(), track)));
        dismiss();
    }


    private void dismiss() {
        fragment.dismiss();
    }

    public void newAlarmClicked(View view) {
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
            Toast.makeText(fragment.getContext(), track.getName() + " a été ajoutée à l'alarme " + alarm.getName(), Toast.LENGTH_SHORT).show();
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
                    public ItemView itemView() {
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
        return "";
    }

    @Bindable
    public String getTitle() {
        if (track != null) return track.getName();
        if (album != null) return album.getName();
        return "";
    }

    @Bindable
    public String getSubtitle() {
        if (track != null) return track.getArtistName();
        if (album != null) return album.getArtist();
        return "";
    }
}
