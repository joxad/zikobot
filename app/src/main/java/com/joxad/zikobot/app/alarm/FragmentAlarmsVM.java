package com.joxad.zikobot.app.alarm;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.fragmentmanager.EventRefreshAlarms;
import com.joxad.zikobot.app.core.viewutils.SnackUtils;
import com.joxad.zikobot.app.databinding.FragmentAlarmsBinding;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.model.Alarm;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 09/03/16.
 */
public class FragmentAlarmsVM extends FragmentBaseVM<FragmentAlarms, FragmentAlarmsBinding> {

    private static final String TAG = FragmentAlarmsVM.class.getSimpleName();

    public ObservableBoolean showTuto;
    public ObservableArrayList<AlarmVM> itemsVM;

    public ItemView itemView = ItemView.of(BR.itemAlarmVM, R.layout.item_alarm);
    ItemTouchHelper swipeToDismissTouchHelper;

    /***
     * @param activity
     * @param binding
     */
    public FragmentAlarmsVM(FragmentAlarms activity, FragmentAlarmsBinding binding) {
        super(activity, binding);
    }

    @Override
    public void onCreate() {
        itemsVM = new ObservableArrayList<>();
        showTuto = new ObservableBoolean(false);
        createSwipeToDismiss();
        swipeToDismissTouchHelper.attachToRecyclerView(binding.alarmRecyclerView);
        loadAlarms();

    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    /**
     * This event allows to refresh the alarms when updating the alarm from the list
     * with the bottom sheet view
     *
     * @param eventRefreshAlarms
     */
    @Subscribe
    public void onEvent(EventRefreshAlarms eventRefreshAlarms) {
        for (AlarmVM alarmVM : itemsVM) {
            if (alarmVM.getModel().getId() == eventRefreshAlarms.getAlarmId()) {
                AlarmManager.getAlarmById(eventRefreshAlarms.getAlarmId()).subscribe(alarmVM::updateModel,
                        throwable -> {

                        });
            }
        }
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
                int position = viewHolder.getAdapterPosition();
                AlarmVM alarmVM = itemsVM.get(position);

                itemsVM.remove(position);

                SnackUtils.showCancelable(binding.getRoot(), R.string.playlist_deleted, view1 -> {
                    itemsVM.add(position, alarmVM);
                }, (snackbar, dismissEvent) -> {
                    if (dismissEvent != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        alarmVM.delete();
                    }
                });
            }
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
            if (alarms.size() == 0) {
                showTuto.set(true);
            }
        }, throwable -> {
            showTuto.set(true);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Method to update the existing alarms if needed
     */
    public void refreshAlarms() {
        for (AlarmVM alarmVM : itemsVM) {
            AlarmManager.getAlarmById(alarmVM.getModel().getId()).subscribe(alarmVM::updateModel);
        }
    }
}
