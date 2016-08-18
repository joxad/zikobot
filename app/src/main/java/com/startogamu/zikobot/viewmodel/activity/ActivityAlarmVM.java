package com.startogamu.zikobot.viewmodel.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.dialog.EventShowDialogAlarm;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.databinding.ActivityAlarmBinding;
import com.startogamu.zikobot.module.zikobot.manager.AlarmManager;
import com.startogamu.zikobot.module.zikobot.manager.AlarmTrackManager;
import com.startogamu.zikobot.module.zikobot.model.Alarm;
import com.startogamu.zikobot.playlist.DialogPlaylistEdit;
import com.startogamu.zikobot.view.activity.ActivityAlarm;
import com.startogamu.zikobot.view.fragment.alarm.DialogFragmentAlarms;
import com.startogamu.zikobot.viewmodel.base.AlarmVM;
import com.startogamu.zikobot.viewmodel.custom.PlayerVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.functions.Action1;

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
    public void init() {
        alarm = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.ALARM));
        initToolbar();
        initMenu();
        initViews();
        initPlayerVM();
        AlarmTrackManager.clear();

    }

    private void initToolbar() {
        String alarmUrl = null;
        if (alarm.getTracks() != null && !alarm.getTracks().isEmpty()) {
            alarmUrl = alarm.getTracks().get(0).getImageUrl();
        }
        ZikoUtils.prepareToolbar(activity, binding.customToolbar, alarm.getName(), alarmUrl);
        ZikoUtils.animateScale(binding.fabPlay);
        ZikoUtils.animateScale(binding.swActivated);
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
                    if (alarmVM.hasTracks() || !AlarmTrackManager.tracks().isEmpty()) {
                        save().subscribe(alarm1 -> {
                            AlarmTrackManager.clear();
                            activity.startActivity(IntentManager.goToWakeUp(alarm1));
                        }, throwable -> {
                            Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                        });
                    } else {
                        Snackbar.make(binding.getRoot(), R.string.add_tracks, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;
        });
        binding.swActivated.setOnClickListener(v -> alarmVM.updateStatus(!alarmVM.isActivated()));


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
    protected void onResume() {
        super.onResume();
        playerVM.onResume();
        if (alarm.getVolume()!=-1){
            AlarmManager.refreshAlarm(alarm.getId()).subscribe(alarm1 -> {
                alarmVM = new AlarmVM(activity, alarm1) {
                    @Override
                    public ItemView itemView() {
                        return ItemView.of(BR.trackVM, R.layout.item_track);
                    }
                };
            });
        }
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
    protected void onPause() {
        super.onPause();
        playerVM.onPause();

    }


    /***
     * Use this method to save the data
     */
    public rx.Observable<Alarm> save() {
        return alarmVM.save();
    }

    /***
     * Delete the alarm
     */
    private void delete() {
        alarmVM.delete();
        activity.finish();
    }

    @Override
    public void destroy() {

    }

    private void showDialogEdit() {
        DialogPlaylistEdit.newInstance(alarm).show(activity.getSupportFragmentManager(), DialogPlaylistEdit.TAG);
    }

}
