package com.startogamu.zikobot.home;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.SeekBar;

import com.joxad.easydatabinding.base.IVM;
import com.joxad.easydatabinding.utils.ObservableString;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.player.TrackChangeEvent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToCurrent;
import com.startogamu.zikobot.core.event.player.EventPlayTrack;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.databinding.ViewPlayerBinding;

import com.startogamu.zikobot.module.lyrics.manager.LyricsManager;
import com.startogamu.zikobot.module.music.PlayerMusicManager;
import com.startogamu.zikobot.core.viewutils.SimpleSeekBarChangeListener;
import com.startogamu.zikobot.localtracks.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 08/06/16.
 */
public class PlayerVM extends BaseObservable implements IVM {

    private Handler handler = new Handler(Looper.getMainLooper());
    private final ViewPlayerBinding binding;
    public ItemView itemView = ItemView.of(BR.trackVM, R.layout.item_track);
    public ItemView itemViewPlayer = ItemView.of(BR.trackVM, R.layout.view_player_current);
    private final Context context;

    private BottomSheetBehavior bottomSheetBehavior;
    private boolean manualChange = false;
    public ObservableBoolean isExpanded = new ObservableBoolean(false);
    public ObservableBoolean isPlaying = new ObservableBoolean(false);
    public ObservableBoolean lyricsSelected = new ObservableBoolean(false);
    public ObservableString currentTrackLyrics = new ObservableString("");
    private PlayerMusicManager playerMusicManager;
    private int previousState;

    public PlayerVM(Context context, ViewPlayerBinding binding) {
        this.context = context;
        this.binding = binding;
        init();
    }

    @Override
    public void init() {

        playerMusicManager = PlayerMusicManager.getInstance();
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
        binding.vpPlayer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (manualChange) {
                    EventBus.getDefault().post(new EventPlayTrack(playerMusicManager.trackVMs().get(position)));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (previousState == ViewPager.SCROLL_STATE_DRAGGING
                        && state == ViewPager.SCROLL_STATE_SETTLING)
                    manualChange = true;

                else if (previousState == ViewPager.SCROLL_STATE_SETTLING
                        && state == ViewPager.SCROLL_STATE_IDLE)
                    manualChange = false;

                previousState = state;
            }
        });

        binding.rbKaraoke.setOnCheckedChangeListener((buttonView, isChecked) -> {
            lyricsSelected.set(isChecked);
        });

        binding.rlProgress.progress.setOnSeekBarChangeListener(new SimpleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    playerMusicManager.seek(progress);
                }
            }
        });
    }

    public void onResume() {
        EventBus.getDefault().register(this);
        updateProgress((int) playerMusicManager.getCurrentTrackVM().getModel().getDuration());
        isExpanded.set(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED);
        binding.rgOptions.setVisibility(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ? View.VISIBLE : View.GONE);
        playerMusicManager.setDurationListener(position -> {
            binding.rlProgress.progress.setProgress((int)position);
            binding.rlProgress.tvDuration.setText(ZikoUtils.readableTime((int)position));
        });
        manualChange = false;
        handler.postDelayed(() -> binding.vpPlayer.setCurrentItem(playerMusicManager.getCurrentSong()),100);
        updateLists();
        updatePlayingStatus(playerMusicManager.isPlaying);
        playerMusicManager.setPlayerStatusListener(this::updatePlayingStatus);
        notifyChange();
    }

    private void updatePlayingStatus(boolean playing) {
        isPlaying.set(playing);
        notifyChange();
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
        playerMusicManager.setDurationListener(null);
        playerMusicManager.setPlayerStatusListener(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(TrackChangeEvent trackChangeEvent) {
        updateLists();
        notifyChange();
        //binding.progress.setm
        LyricsManager.getInstance().search(trackChangeEvent.getTrack().getArtistName(),
                trackChangeEvent.getTrack().getName()).subscribe(result -> {
            currentTrackLyrics.set(result.getResult().getLyrics());
        }, throwable -> {
            currentTrackLyrics.set(throwable.getLocalizedMessage());
        });
        binding.vpPlayer.setCurrentItem(playerMusicManager.getCurrentSong(), true);
        updateProgress((int) trackChangeEvent.getTrack().getDuration());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(EventAddTrackToCurrent addTrackToCurrent) {
        updateLists();
        notifyChange();
    }

    private void updateLists() {
        if (binding.rvTracks.getAdapter()==null)return;
        binding.rvTracks.getAdapter().notifyDataSetChanged();
        binding.vpPlayer.getAdapter().notifyDataSetChanged();
    }

    @Bindable
    public ArrayList<TrackVM> getTrackVMs() {
        return playerMusicManager.trackVMs();
    }

    /***
     * @param view
     */
    public void onPlayPauseClicked(View view) {
        playerMusicManager.playOrResume();
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
        return playerMusicManager.trackVMs().isEmpty();
    }

    @Bindable
    public TrackVM getTrackVM() {
        return playerMusicManager.getCurrentTrackVM();
    }


    private void updateProgress(int dur) {
        binding.rlProgress.tvDurationMax.setText(ZikoUtils.readableTime(dur));
        binding.rlProgress.progress.setMax(dur);
    }

    public void close() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
