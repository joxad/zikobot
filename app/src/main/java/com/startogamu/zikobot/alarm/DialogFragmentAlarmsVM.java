package com.startogamu.zikobot.alarm;

import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.joxad.easydatabinding.fragment.DialogFragmentBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.alarm.EventAlarmSelect;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.DialogFragmentAlarmsBinding;
import com.startogamu.zikobot.core.model.Alarm;
import com.startogamu.zikobot.core.model.Track;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 09/03/16.
 */
public class DialogFragmentAlarmsVM extends DialogFragmentBaseVM<DialogFragmentAlarms, DialogFragmentAlarmsBinding> {
    private static final String TAG = DialogFragmentAlarmsVM.class.getSimpleName();
    public ObservableArrayList<AlarmVM> itemsVM;

    public ItemView itemView = ItemView.of(BR.itemAlarmVM, R.layout.item_alarm_dialog);

    @Nullable
    Track track;

    /***
     * @param fragment
     * @param binding
     */
    public DialogFragmentAlarmsVM(DialogFragmentAlarms fragment, DialogFragmentAlarmsBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {
        Parcelable parcelable = fragment.getArguments().getParcelable(EXTRA.TRACK);
        track = (parcelable != null ? Parcels.unwrap(parcelable) : null);
        itemsVM = new ObservableArrayList<>();
        loadAlarms();

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onPause() {
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
            fragment.dismiss();
        }, throwable -> {
            Toast.makeText(fragment.getContext(), "Oops j'ai eu un souci", Toast.LENGTH_SHORT).show();

        });

    }

    public void newAlarmClicked(View view) {
        Alarm alarm = new Alarm();
        AlarmTrackManager.clear();
        AlarmTrackManager.selectTrack(track);
        AlarmManager.saveAlarm(alarm, AlarmTrackManager.tracks()).subscribe(alarm1 -> {
            Toast.makeText(fragment.getContext(), track.getName() + " a été ajoutée à l'alarme " + alarm.getName(), Toast.LENGTH_SHORT).show();
            fragment.dismiss();
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


    @Override
    public void destroy() {

        EventBus.getDefault().unregister(this);
    }

}
