package com.startogamu.zikobot.alarm;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.dialog.EventShowDialogAlarm;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.model.Alarm;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.databinding.ActivityAlarmBinding;
import com.startogamu.zikobot.player.PlayerVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 29/05/16.
 */
public class ActivityAlarmVM extends ActivityBaseVM<ActivityAlarm, ActivityAlarmBinding> {
    private static String TAG = ActivityAlarmVM.class.getSimpleName();


    public AlarmVM alarmVM;
    public PlayerVM playerVM;
    Alarm alarm;

    /***
     * @param activity
     * @param binding
     */
    public ActivityAlarmVM(ActivityAlarm activity, ActivityAlarmBinding binding) {
        super(activity, binding);
    }

    @Override
    public void onCreate() {
        alarm = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.ALARM));
        initToolbar();
        initMenu();
        initViews();
        initPlayerVM();

    }

    private void initToolbar() {
        String alarmUrl = null;
        if (alarm.getTracks() != null && !alarm.getTracks().isEmpty()) {
            alarmUrl = alarm.getTracks().get(0).getImageUrl();
        }
        ZikoUtils.prepareToolbar(activity, binding.customToolbar, alarm.getName(), alarmUrl);
        ZikoUtils.animateScale(binding.fabPlay);
        ZikoUtils.animateScale(binding.swActivated);
        ZikoUtils.animateFade(binding.customToolbar.rlOverlay);
        binding.customToolbar.mainCollapsing.setOnClickListener(v -> {
            showDialogEdit();
        });
    }


    private void initMenu() {
        binding.customToolbar.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    showDialogEdit();
                    break;
                case R.id.action_delete:
                    delete();
                    break;
                case R.id.action_play:
                    if (alarmVM.hasTracks()) {
                        activity.startActivity(IntentManager.goToWakeUp(alarmVM.getModel()));
                    } else {
                        Snackbar.make(binding.getRoot(), R.string.add_tracks, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;
        });
        binding.swActivated.setOnClickListener(v -> {
            alarmVM.updateStatus(!alarmVM.isActivated());
        });
    }


    /***
     *
     */
    private void initViews() {
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                alarmVM.removeTrack(viewHolder.getAdapterPosition());
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(binding.rvTracks);
        binding.rvTracks.setNestedScrollingEnabled(false);

    }

    /***
     *
     */

    private void initPlayerVM() {
        playerVM = new PlayerVM(activity, binding.viewPlayer);
    }


    @Override
    public void onResume() {
        super.onResume();
        playerVM.onResume();
        alarmVM = new AlarmVM(activity, alarm) {
            @Override
            public ItemView itemView() {
                return ItemView.of(BR.trackVM, R.layout.item_track);
            }
        };
        alarmVM.refreshTracks();

    }


    @Subscribe
    public void onEvent(EventShowDialogAlarm event) {
        DialogFragmentAlarms dialogFragmentAlarms = DialogFragmentAlarms.newInstance(event.getModel());
        dialogFragmentAlarms.show(activity.getSupportFragmentManager(), DialogFragmentAlarms.TAG);
    }

    /**
     * Play all the tracks of the album
     *
     * @param view
     */
    public void onPlay(View view) {
        EventBus.getDefault().post(new EventAddTrackToPlayer(alarmVM.tracksVms));
    }


    @Override
    public void onPause() {
        super.onPause();
        playerVM.onPause();

    }


    /***
     * Delete the alarm
     */
    private void delete() {
        alarmVM.delete();
        activity.finish();
    }

    private void showDialogEdit() {

        DialogPlaylistEdit dialogPlaylistEdit = DialogPlaylistEdit.newInstance(alarmVM.getModel());
        dialogPlaylistEdit.show(activity.getSupportFragmentManager(), DialogPlaylistEdit.TAG);
        dialogPlaylistEdit.setOnDismissListener(this::updateAlarm);

    }

    private void updateAlarm() {
        AlarmManager.refreshAlarm(alarm.getId()).subscribe(alarm1 -> {
            alarmVM.updateName(alarm1.getName());
            binding.customToolbar.mainCollapsing.setTitle(alarm1.getName());
        });
    }

    @Override
    protected boolean onBackPressed() {
        if (playerVM.onBackPressed()) {
            binding.fabPlay.setVisibility(View.GONE);
            return true;
        }
        return false;
    }
}
