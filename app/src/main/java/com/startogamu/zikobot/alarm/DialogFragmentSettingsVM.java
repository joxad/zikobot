package com.startogamu.zikobot.alarm;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.joxad.easydatabinding.bottomsheet.DialogBottomSheetBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.album.AlbumVM;
import com.startogamu.zikobot.core.event.alarm.EventAlarmSelect;
import com.startogamu.zikobot.core.event.player.EventAddTrackToCurrent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToEndOfCurrent;
import com.startogamu.zikobot.core.model.Alarm;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.core.module.localmusic.model.LocalAlbum;
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

    public TrackVM trackVM = null;
    private LocalAlbum album;
    private AlbumVM albumVM = null;

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
            trackVM = new TrackVM(fragment.getContext(), track);
        }
        if (fragment.getArguments().containsKey(EXTRA.LOCAL_ALBUM)) {
            Parcelable parcelableAlbum = fragment.getArguments().getParcelable(EXTRA.LOCAL_ALBUM);
            album = (parcelableAlbum != null ? Parcels.unwrap(parcelableAlbum) : null);
            albumVM = new AlbumVM(fragment.getContext(), album);
        }
        itemsVM = new ObservableArrayList<>();
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
        AlarmTrackManager.selectTrack(track);
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
        EventBus.getDefault().post(new EventAddTrackToCurrent(trackVM));
        dismiss();
    }

    public void addTrackToEndOfCurrent(View view) {
        EventBus.getDefault().post(new EventAddTrackToEndOfCurrent(trackVM));
        dismiss();
    }


    private void dismiss() {
        fragment.dismiss();
    }

    public void newAlarmClicked(View view) {
        Alarm alarm = new Alarm();
        AlarmTrackManager.clear();
        AlarmTrackManager.selectTrack(track);
        AlarmManager.saveAlarm(alarm, AlarmTrackManager.tracks()).subscribe(alarm1 -> {
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
        if (trackVM != null) return trackVM.getImageUrl();
        if (albumVM != null) return albumVM.getImageUrl();
        return "";
    }

    @Bindable
    public String getTitle() {
        if (trackVM != null) return trackVM.getName();
        if (albumVM != null) return albumVM.getName();
        return "";
    }

    @Bindable
    public String getSubtitle() {
        if (trackVM != null) return trackVM.getArtistName();
        if (albumVM != null) return albumVM.getArtist();
        return "";
    }
}
