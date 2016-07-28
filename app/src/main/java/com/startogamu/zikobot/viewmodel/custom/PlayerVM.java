package com.startogamu.zikobot.viewmodel.custom;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.SeekBar;

import com.joxad.easydatabinding.base.IVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.TrackChangeEvent;
import com.startogamu.zikobot.core.event.player.DurationEvent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToCurrent;
import com.startogamu.zikobot.core.event.player.EventPlayTrack;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.databinding.ViewPlayerBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.view.listener.SimpleSeekBarChangeListener;
import com.startogamu.zikobot.viewmodel.base.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 08/06/16.
 */
public class PlayerVM extends BaseObservable implements IVM {

    private final ViewPlayerBinding binding;
    public ItemView itemView = ItemView.of(BR.trackVM, R.layout.item_track);
    public ItemView itemViewPlayer = ItemView.of(BR.trackVM, R.layout.view_player_current);
    private final Context context;

    private BottomSheetBehavior bottomSheetBehavior;
    private boolean manualChange = false;
    public ObservableBoolean isExpanded = new ObservableBoolean(false);

    public PlayerVM(Context context, ViewPlayerBinding binding) {
        this.context = context;
        this.binding = binding;
        init();
    }

    @Override
    public void init() {
        binding.vpPlayer.setOffscreenPageLimit(20);
        bottomSheetBehavior = BottomSheetBehavior.from(binding.getRoot());
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                isExpanded.set(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED);
                notifyPropertyChanged(BR.playerVM);

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        Injector.INSTANCE.playerComponent().inject(this);
        binding.vpPlayer.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                manualChange = true;
                EventBus.getDefault().post(new EventPlayTrack(Injector.INSTANCE.playerComponent().manager().trackVMs().get(position)));
            }
        });

        binding.rlProgress.progress.setOnSeekBarChangeListener(new SimpleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Injector.INSTANCE.playerComponent().manager().seek(progress);
                }
            }
        });
    }

    public void onResume() {
        EventBus.getDefault().register(this);
        isExpanded.set(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED);
        binding.rgOptions.setVisibility(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ? View.VISIBLE : View.GONE);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    //eventbus that updates UI
    public void onEvent(DurationEvent event) {
        //seekbar or any other ui element
        int millis = event.getPosition();
        binding.rlProgress.progress.setProgress(millis);
        binding.rlProgress.tvDuration.setText(ZikoUtils.readableTime(millis));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(TrackChangeEvent trackChangeEvent) {
        if (manualChange) {
            manualChange = false;
            return;
        }
        notifyChange();
        //binding.progress.setm
        binding.vpPlayer.getAdapter().notifyDataSetChanged();
        binding.vpPlayer.setCurrentItem(Injector.INSTANCE.playerComponent().manager().getCurrentSong(), true);
        binding.rlProgress.tvDurationMax.setText(ZikoUtils.readableTime((int) trackChangeEvent.getTrack().getDuration()));
        binding.rlProgress.progress.setMax((int) trackChangeEvent.getTrack().getDuration());
    }


    @Subscribe
    public void onReceive(EventAddTrackToCurrent trackChangeEvent) {
        notifyChange();
        binding.vpPlayer.getAdapter().notifyDataSetChanged();
    }


    /***
     * @param view
     */
    public void onPlayPauseClicked(View view) {
        Injector.INSTANCE.playerComponent().manager().playOrResume();
        notifyChange();
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }


    @Bindable
    public boolean isPlaying() {
        return Injector.INSTANCE.playerComponent().manager().isPlaying();
    }

    @Bindable
    public int getImageState() {
        if (Injector.INSTANCE.playerComponent().manager().isPlaying()) {
            return R.drawable.ic_pause;
        } else {
            return R.drawable.ic_play_arrow;
        }
    }


    @Bindable
    public ArrayList<TrackVM> getTrackVMs() {
        return Injector.INSTANCE.playerComponent().manager().trackVMs();
    }

    @Bindable
    public boolean getIsEmpty() {
        return Injector.INSTANCE.playerComponent().manager().trackVMs().isEmpty();
    }

    @Bindable
    public TrackVM getTrackVM() {
        return Injector.INSTANCE.playerComponent().manager().getCurrentTrackVM();
    }

}
