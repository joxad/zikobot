package com.joxad.zikobot.app.alarm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.fragmentmanager.IntentManager;
import com.joxad.zikobot.app.core.fragmentmanager.NavigationManager;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.core.utils.ZikoUtils;
import com.joxad.zikobot.app.databinding.ActivityAlarmBinding;
import com.joxad.zikobot.app.player.PlayerVM;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.data.model.Alarm;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 29/05/16.
 */
public class ActivityAlarmVM extends ActivityBaseVM<ActivityAlarm, ActivityAlarmBinding> {

    public AlarmVM alarmVM;
    public PlayerVM playerVM;
    Alarm alarm;
    NavigationManager navigationManager;

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
        alarmVM = new AlarmVM(activity, alarm) {
            @Override
            public ItemView itemView() {
                return ItemView.of(BR.trackVM, R.layout.item_track);
            }
        };
        initPlayerVM();
        initToolbar();
        navigationManager = new NavigationManager(activity);
        initMenu();
        initViews();
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
        binding.customToolbar.mainCollapsing.setOnClickListener(v -> showDialogEdit());
    }


    /**
     *
     */
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
                    startAlarm();
                    break;
                default:
                    break;
            }
            return false;
        });
        binding.swActivated.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                Intent intent = new Intent();
                String packageName = activity.getPackageName();
                PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                  //  intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
             //   else {
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    activity.startActivity(intent);
                }

            }


            alarmVM.updateStatus(!alarmVM.isActivated());


        });
    }

    /***
     *
     */
    private void startAlarm() {
        if (alarmVM.hasTracks()) {
            activity.startActivity(IntentManager.goToWakeUp(alarmVM.getModel()));
        } else {
            Snackbar.make(binding.getRoot(), R.string.add_tracks, Snackbar.LENGTH_SHORT).show();
        }
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
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();

        alarmVM.refreshTracks();

    }

    @Override
    public void onResume() {
        super.onResume();
        navigationManager.onResume();
        playerVM.onResume();

    }


    /**
     * Play all the tracks of the album
     *
     * @param view
     */
    public void onPlay(@SuppressWarnings("unused") View view) {
        EventBus.getDefault().post(new EventAddList(alarmVM.tracksVms));
    }


    @Override
    public void onPause() {
        super.onPause();
        playerVM.onPause();
        navigationManager.onPause();
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
            binding.swActivated.setVisibility(View.GONE);
            return true;
        }
        return false;
    }
}
