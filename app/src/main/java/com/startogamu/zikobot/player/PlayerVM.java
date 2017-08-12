package com.startogamu.zikobot.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.joxad.easydatabinding.base.IVM;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.db.model.ZikoTrack;
import com.joxad.zikobot.data.player.PlayerService;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.databinding.PlayerViewBottomBinding;
import com.startogamu.zikobot.home.track.TrackVM;

/**
 * Created by Jocelyn on 12/12/2016.
 */

public class PlayerVM extends BaseObservable implements IVM {

    public final ObservableBoolean isBound = new ObservableBoolean(false);
    public final ObservableBoolean isExpanded = new ObservableBoolean(false);
    private final PlayerViewBottomBinding binding;
    public ObservableField<Integer> seekBarValue = new ObservableField<>(0);
    //public ItemBinding itemView = ItemBinding.of(BR.trackVM, R.layout.item_track_player);
    public ObservableBoolean showList;
    private ServiceConnection musicConnection;
    private AppCompatActivity activity;
    private PlayerService playerService;
    private Intent intent;
    private BottomSheetBehavior<View> behavior;

    public PlayerVM(AppCompatActivity activity, PlayerViewBottomBinding binding) {
        this.activity = activity;
        this.binding = binding;
        onCreate(null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstance) {
        showList = new ObservableBoolean(false);
        intent = new Intent(activity, PlayerService.class);
        if (binding == null)
            return;
        behavior = BottomSheetBehavior.from((CardView) binding.getRoot().getParent());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    isExpanded.set(true);
                else {
                    if (isExpanded.get())
                        isExpanded.set(false);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    @Override
    public void onResume() {
        if (isBound.get())
            return;
        musicConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) iBinder;
                playerService = binder.getService();
                isBound.set(true);
                refresh();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isBound.set(false);
            }
        };
        activity.bindService(intent, musicConnection, Context.BIND_AUTO_CREATE);
        if (binding != null)
            rotateCD();
    }


    private void refresh() {
        notifyChange();
    }


    /**
     * @param view
     */
    public void playPause(@SuppressWarnings("unused") View view) {
        if (isPlaying())
            playerService.pause();
        else playerService.resume();
        //notifyPropertyChanged(BR.playing);
    }


    @Override
    public void onPause() {
        isBound.set(false);
        activity.unbindService(musicConnection);
    }

    @Override
    public void onDestroy() {
    }


    @Bindable
    public boolean isPlaying() {
        if (!isBound.get())
            return false;
        return playerService.playing;
    }


    @Bindable
    public int getPosition() {
        if (!isBound.get())
            return 0;
        return seekBarValue.get();
    }

    @Bindable
    public int getPositionMax() {
        if (!isBound.get())
            return 0;
        return playerService.positionMax();
    }

    public void onValueChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
        seekBarValue.set(progresValue);
        if (fromUser)
            playerService.seekTo(progresValue);
    }

    /**
     * Handle the back press
     *
     * @return
     */
    public boolean onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return false;
        }
        return true;
    }


    /**
     *
     */
    private void rotateCD() {
        binding.layoutVinyl.rlPlayer.animate().rotationBy(isPlaying() ? 2f : 0).
                setDuration(50).withEndAction(this::rotateCD);
    }

    /**
     * Trick click to avoid to click items below the player
     *
     * @param view
     */
    public void trickClick(@SuppressWarnings("unused") View view) {

    }

    /***
     * @param view
     */
    public void showListTracks(@SuppressWarnings("unused") View view) {
        showList.set(!showList.get());
    }

    public void clickVinyl(View view) {
        if (!AppPrefs.bonusModeActivated()) {
            AppPrefs.bonusMode(AppPrefs.bonusMode() + 1);
        } else {
            if (AppPrefs.bonusMode() == 9)
                Toast.makeText(activity, "Bonus mode activated, have fun :)", Toast.LENGTH_SHORT).show();
        }
    }

    @Bindable
    public TrackVM getCurrentTrackVM() {
        if (playerService !=null && playerService.getCurrentTrack()!=null)
        return new TrackVM(activity, playerService.getCurrentTrack()); else {
            return new TrackVM(activity, ZikoTrack.empty());
        }
    }

}