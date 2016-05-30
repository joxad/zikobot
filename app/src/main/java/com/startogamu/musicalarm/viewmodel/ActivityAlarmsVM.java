package com.startogamu.musicalarm.viewmodel;

import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.AppPrefs;
import com.startogamu.musicalarm.databinding.ActivityAlarmsBinding;
import com.startogamu.musicalarm.module.alarm.manager.AlarmManager;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.view.Henson;
import com.startogamu.musicalarm.view.activity.ActivityAlarms;
import com.startogamu.musicalarm.viewmodel.base.AlarmVM;

import java.io.UnsupportedEncodingException;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscriber;

/**
 * Created by josh on 09/03/16.
 */
public class ActivityAlarmsVM extends ActivityBaseVM<ActivityAlarms, ActivityAlarmsBinding> {

    private static final String TAG = ActivityAlarmsVM.class.getSimpleName();

    public ObservableArrayList<AlarmVM> itemsVM;

    public ItemView itemView = ItemView.of(BR.itemAlarmVM, R.layout.item_alarm);

    /***
     * @param activity
     * @param binding
     */
    public ActivityAlarmsVM(ActivityAlarms activity, ActivityAlarmsBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        Injector.INSTANCE.spotifyAuth().inject(this);
        activity.setSupportActionBar(binding.toolbar);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    activity.goToSettingsActivity();
                    return true;
                case R.id.action_about:
                    new LibsBuilder()
                            .withAboutAppName(activity.getString(R.string.about))
                            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                            .start(activity);
                    return true;
                default:
                    return true;
            }
        });
        itemsVM = new ObservableArrayList<>();
        if (Prefs.contains(AppPrefs.ACCESS_CODE)) {
            try {
                refreshAccessToken();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // callback for drag-n-drop, false to skip this feature
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                itemsVM.get(viewHolder.getAdapterPosition()).delete();
                itemsVM.remove(viewHolder.getAdapterPosition());

            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(binding.alarmRecyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAlarms();
    }

    /***
     *
     */
    public void loadAlarms() {
        AlarmManager.loadAlarms().subscribe(new Subscriber<List<Alarm>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Alarm> alarms) {
                itemsVM.clear();
                for (Alarm alarm : alarms) {
                    itemsVM.add(new AlarmVM(activity, alarm));
                }

            }
        });
    }

    private void refreshAccessToken() throws UnsupportedEncodingException {
        Injector.INSTANCE.spotifyAuth().manager().refreshToken(activity, () -> {

        });
    }


    public void addAlarm(View view) {
        activity.startActivity(Henson.with(activity)
                .gotoActivityAlarm2().alarm(new Alarm()).build());
    }



    @Override
    public void destroy() {

    }
}
