package com.startogamu.musicalarm.viewmodel.activity;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
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
import com.startogamu.musicalarm.module.alarm.model.Alarm;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.view.Henson;
import com.startogamu.musicalarm.view.activity.ActivityAlarms;
import com.startogamu.musicalarm.viewmodel.base.AlarmVM;

import java.io.UnsupportedEncodingException;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.functions.Action1;

/**
 * Created by josh on 09/03/16.
 */
public class ActivityAlarmsVM extends ActivityBaseVM<ActivityAlarms, ActivityAlarmsBinding> {

    private static final String TAG = ActivityAlarmsVM.class.getSimpleName();

    public ObservableBoolean showTuto;
    public ObservableArrayList<AlarmVM> itemsVM;

    public ItemView itemView = ItemView.of(BR.itemAlarmVM, R.layout.item_alarm);
    ItemTouchHelper swipeToDismissTouchHelper;

    /***
     * @param activity
     * @param binding
     */
    public ActivityAlarmsVM(ActivityAlarms activity, ActivityAlarmsBinding binding) {
        super(activity, binding);
        Injector.INSTANCE.spotifyAuth().inject(this);
    }

    @Override
    public void init() {
        if (!AppPrefs.isFirstStart()) {
            activity.startActivity(Henson.with(activity).gotoActivityFirstStart().build());
        }
        initToolbar();
        itemsVM = new ObservableArrayList<>();
        showTuto = new ObservableBoolean(false);
        if (Prefs.contains(AppPrefs.SPOTIFY_ACCESS_CODE)) {
            try {
                refreshAccessToken();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        createSwipeToDismiss();
        swipeToDismissTouchHelper.attachToRecyclerView(binding.alarmRecyclerView);
    }

    private void createSwipeToDismiss() {
        swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
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
    }

    /***
     *
     */
    private void initToolbar() {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        showTuto.set(false);
        loadAlarms();
    }

    /***
     *
     */
    public void loadAlarms() {
        AlarmManager.loadAlarms().subscribe(alarms -> {
            itemsVM.clear();
            for (Alarm alarm : alarms) {
                itemsVM.add(new AlarmVM(activity, alarm));
            }
            if (alarms.size()==0) {
                showTuto.set(true);
            }
        }, throwable -> {
            showTuto.set(true);
        });
    }

    private void refreshAccessToken() throws UnsupportedEncodingException {
        Injector.INSTANCE.spotifyAuth().manager().refreshToken(activity, () -> {

        });
    }


    public void addAlarm(View view) {
        activity.startActivity(Henson.with(activity)
                .gotoActivityAlarm().alarm(new Alarm()).build());
    }


    @Override
    public void destroy() {

    }
}
