package com.joxad.zikobot.app.wakeup;

import android.animation.Animator;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.media.AudioManager;
import android.view.View;
import android.view.WindowManager;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.alarm.AlarmVM;
import com.joxad.zikobot.app.core.mock.Mock;
import com.joxad.zikobot.app.core.utils.AnimationEndListener;
import com.joxad.zikobot.app.core.utils.Constants;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.databinding.ActivityWakeUpBinding;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.player.PlayerVM;
import com.joxad.zikobot.app.player.alarm.WakePlayerVM;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.app.player.event.EventForceRefreshPlayerSpotify;
import com.joxad.zikobot.app.player.event.EventStopPlayer;
import com.joxad.zikobot.data.model.Alarm;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 31/05/16.
 */
public class ActivityWakeUpVM extends ActivityBaseVM<ActivityWakeUp, ActivityWakeUpBinding> {

    public WakePlayerVM playerVM;
    public AlarmVM alarmVM;
    public TrackVM trackVM;
    Alarm alarm;
    AudioManager am;


    /***
     * @param activity
     * @param binding
     */
    public ActivityWakeUpVM(ActivityWakeUp activity, ActivityWakeUpBinding binding) {
        super(activity, binding);

    }

    @Override
    public void onCreate() {
        am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//For Normal mode
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        alarm = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.ALARM));
        playerVM = new WakePlayerVM(activity, null, alarm);

        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> onBackPressed());
        activity.setTitle(alarm.getName());
        alarmVM = new AlarmVM(activity, alarm) {
            @Override
            public ItemView itemView() {
                return ItemView.of(BR.trackVM, R.layout.item_alarm_track);

            }
        };
        binding.setAlarmVM(alarmVM);
        if (!alarm.getTracks().isEmpty()) {
            trackVM = new TrackVM(activity, Mock.track(activity));
            binding.setAlarmTrackVM(trackVM);
        }
        am.setStreamVolume(AudioManager.STREAM_MUSIC, alarm.getVolume(), alarm.getVolume());

        rotateCD();
    }

    @Override
    public void onResume() {
        super.onResume();
        playerVM.onResume();
    }


    /**
     *
     */
    private void rotateCD() {
        binding.layoutPlayer.layoutVinyl.rlPlayer.animate().rotationBy(playerVM.isPlaying() ? Constants.ROTATION : 0)
                .setDuration(50).setListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rotateCD();
            }
        });
    }

    /***
     * @param view
     */
    public void stop(@SuppressWarnings("unused") View view) {
        EventBus.getDefault().post(new EventStopPlayer());

    }


    @Override
    protected boolean onBackPressed() {
        stop(null);
        return super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        playerVM.onPause();
    }

}
