package com.startogamu.zikobot.viewmodel.fragment.alarm;

import android.databinding.ObservableArrayList;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.fragment.DialogFragmentBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.analytics.AnalyticsManager;
import com.startogamu.zikobot.core.event.alarm.EventAlarmSelect;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.DialogFragmentAlarmsBinding;
import com.startogamu.zikobot.alarm.AlarmManager;
import com.startogamu.zikobot.alarm.AlarmTrackManager;
import com.startogamu.zikobot.module.zikobot.model.Alarm;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.view.fragment.alarm.DialogFragmentAlarms;
import com.startogamu.zikobot.viewmodel.base.AlarmVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    @InjectExtra(EXTRA.TRACK)
    Track track;

    /***
     * @param fragment
     * @param binding
     */
    public DialogFragmentAlarmsVM(DialogFragmentAlarms fragment, DialogFragmentAlarmsBinding binding) {
        super(fragment, binding);
        Dart.inject(this, fragment.getArguments());
    }

    @Override
    public void init() {
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
        EventBus.getDefault().cancelEventDelivery(eventAlarmSelect) ;

        Alarm alarm = eventAlarmSelect.getModel();
        AlarmTrackManager.clear();
        AlarmTrackManager.selectTrack(track);
        ArrayList<Track> allTracks = new ArrayList<>();
        allTracks.addAll(alarm.getTracks());
        allTracks.addAll(AlarmTrackManager.tracks());
        AlarmManager.saveAlarm(alarm, allTracks).subscribe(alarm1 -> {
            AnalyticsManager.logCreateAlarm(alarm,false);
            Toast.makeText(fragment.getContext(), track.getName() + " a été ajoutée à la playlist " + alarm.getName(), Toast.LENGTH_SHORT).show();
            fragment.dismiss();
        }, throwable -> {
            Toast.makeText(fragment.getContext(), "Oops j'ai eu un souci", Toast.LENGTH_SHORT).show();

        });

    }

    public void newAlarmClicked(View view){
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
