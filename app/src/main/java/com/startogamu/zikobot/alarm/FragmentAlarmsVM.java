package com.startogamu.zikobot.alarm;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.databinding.FragmentAlarmsBinding;

import com.startogamu.zikobot.module.spotify_auth.manager.SpotifyAuthManager;
import com.startogamu.zikobot.module.zikobot.model.Alarm;

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
    public void init() {
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

    private void refreshAccessToken() throws UnsupportedEncodingException {
       SpotifyAuthManager.getInstance().refreshToken(fragment.getContext(), () -> {

        });
    }

    @Override
    public void destroy() {

    }

}
