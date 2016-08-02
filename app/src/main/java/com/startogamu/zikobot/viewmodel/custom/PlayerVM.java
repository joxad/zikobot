package com.startogamu.zikobot.viewmodel.custom;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.SeekBar;

import com.joxad.easydatabinding.base.IVM;
import com.joxad.easydatabinding.utils.ObservableString;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.TrackChangeEvent;
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
    public ObservableArrayList<TrackVM> trackVMs;
    public ObservableBoolean isPlaying = new ObservableBoolean(false);
    public ObservableBoolean lyricsSelected = new ObservableBoolean(false);
    public ObservableString currentTrackLyrics = new ObservableString("");

    public PlayerVM(Context context, ViewPlayerBinding binding) {
        this.context = context;
        this.binding = binding;
        init();
    }

    @Override
    public void init() {
        trackVMs = new ObservableArrayList<>();
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

        binding.rbKaraoke.setOnCheckedChangeListener((buttonView, isChecked) -> {
            lyricsSelected.set(isChecked);
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
        updateProgress((int) Injector.INSTANCE.playerComponent().manager().getCurrentTrackVM().getModel().getDuration());
        isExpanded.set(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED);
        binding.rgOptions.setVisibility(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ? View.VISIBLE : View.GONE);
        trackVMs.clear();
        trackVMs.addAll(Injector.INSTANCE.playerComponent().manager().trackVMs());
        Injector.INSTANCE.playerComponent().manager().setDurationListener(position -> {
            binding.rlProgress.progress.setProgress(position);
            binding.rlProgress.tvDuration.setText(ZikoUtils.readableTime(position));
        });
        updatePlayingStatus(Injector.INSTANCE.playerComponent().manager().isPlaying);
        Injector.INSTANCE.playerComponent().manager().setPlayerStatusListener(this::updatePlayingStatus);
        notifyChange();
    }

    private void updatePlayingStatus(boolean playing) {
        isPlaying.set(playing);
        notifyChange();
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
        Injector.INSTANCE.playerComponent().manager().setDurationListener(null);
        Injector.INSTANCE.playerComponent().manager().setPlayerStatusListener(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(TrackChangeEvent trackChangeEvent) {
        if (manualChange) {
            manualChange = false;
            return;
        }
        notifyChange();
        //binding.progress.setm
        trackVMs.clear();
        trackVMs.addAll(Injector.INSTANCE.playerComponent().manager().trackVMs());
        binding.rvTracks.invalidate();
        binding.vpPlayer.invalidate();
        Injector.INSTANCE.lyricsComponent().lyricsManager().search(trackChangeEvent.getTrack().getArtistName(),
                trackChangeEvent.getTrack().getName()).subscribe(result -> {
            currentTrackLyrics.set(result.getResult().getLyrics());
        }, throwable -> {
            currentTrackLyrics.set(throwable.getLocalizedMessage());
        });
        binding.vpPlayer.setCurrentItem(Injector.INSTANCE.playerComponent().manager().getCurrentSong(), true);
        updateProgress((int) trackChangeEvent.getTrack().getDuration());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(EventAddTrackToCurrent addTrackToCurrent) {
        trackVMs.add(addTrackToCurrent.getTrackVM());
        binding.rvTracks.invalidate();
        binding.vpPlayer.invalidate();
        notifyChange();

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
    public int getImageState() {
        if (isPlaying.get()) {
            return R.drawable.ic_pause;
        } else {
            return R.drawable.ic_play_arrow;
        }
    }

    @Bindable
    public boolean getIsEmpty() {
        return Injector.INSTANCE.playerComponent().manager().trackVMs().isEmpty();
    }

    @Bindable
    public TrackVM getTrackVM() {
        return Injector.INSTANCE.playerComponent().manager().getCurrentTrackVM();
    }


    private void updateProgress(int dur) {
        binding.rlProgress.tvDurationMax.setText(ZikoUtils.readableTime(dur));
        binding.rlProgress.progress.setMax(dur);
    }

    public void close() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
