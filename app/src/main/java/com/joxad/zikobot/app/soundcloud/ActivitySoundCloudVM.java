package com.joxad.zikobot.app.soundcloud;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.alarm.DialogFragmentSettings;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.core.utils.ZikoUtils;
import com.joxad.zikobot.app.databinding.ActivitySoundCloudBinding;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.player.PlayerVM;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.data.event.dialog.EventShowDialogSettings;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudTrack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by josh on 09/08/16.
 */
public class ActivitySoundCloudVM extends ActivityBaseVM<ActivitySoundCloud, ActivitySoundCloudBinding> {


    private static final String TAG = ActivitySoundCloudVM.class.getSimpleName();
    public ItemBinding itemViewTrack = ItemBinding.of(BR.trackVM, R.layout.item_track);

    public PlayerVM playerVM;
    public ObservableArrayList<TrackVM> tracks;

    public SoundCloudPlaylist soundCloudPlaylist;

    /***
     * @param activity
     * @param binding
     */
    public ActivitySoundCloudVM(ActivitySoundCloud activity, ActivitySoundCloudBinding binding,@Nullable Bundle saved) {
        super(activity, binding,saved);
    }

    @Override
    public void onCreate(@Nullable Bundle saved) {
        soundCloudPlaylist = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.PLAYLIST));
        tracks = new ObservableArrayList<>();
        binding.rv.setNestedScrollingEnabled(false);
        initToolbar();
        initPlayerVM();
        initMenu();
    }


    private void initMenu() {
        binding.customToolbar.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    showDialogEdit();
                    break;

            }
            return false;
        });

    }
    private void showDialogEdit() {
        DialogFragmentSettings dialogFragmentSettings = DialogFragmentSettings.newInstance(soundCloudPlaylist);
        dialogFragmentSettings.show(activity.getSupportFragmentManager(), DialogFragmentSettings.TAG);
    }
    /***
     * Init the toolar
     */
    private void initToolbar() {
        String playlistImage = null;
        try {
            playlistImage = soundCloudPlaylist.getSoundCloudTracks().get(0).getArtworkUrl();
        } catch (Exception e) {
            Log.e("SC", e.getLocalizedMessage());
        }
        ZikoUtils.prepareToolbar(activity, binding.customToolbar, soundCloudPlaylist.getTitle(), playlistImage);
        ZikoUtils.animateScale(binding.fabPlay);
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
        EventBus.getDefault().register(this);
        playerVM.onResume();
        loadTracks(soundCloudPlaylist);
        //TODO getinfos on the album
    }


    @Subscribe
    public void onEvent(EventShowDialogSettings event) {
        DialogFragmentSettings dialogFragmentSettings = DialogFragmentSettings.newInstance(event.getModel());
        dialogFragmentSettings.show(activity.getSupportFragmentManager(), DialogFragmentSettings.TAG);

    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        playerVM.onPause();
    }

    /***
     * FInd the list of track from the playlist
     *
     * @param playlist
     */
    private void loadTracks(SoundCloudPlaylist playlist) {
        tracks.clear();
        for (SoundCloudTrack track : playlist.getSoundCloudTracks()) {
            tracks.add(new TrackVM(activity, Track.from(track, activity.getString(R.string.soundcloud_id))));
        }
    }

    /**
     * Play all the tracks of the album
     *
     * @param view
     */
    public void onPlay(View view) {
        EventBus.getDefault().post(new EventAddList(tracks));
    }

    @Override
    protected boolean onBackPressed() {
        if (playerVM.onBackPressed()) {
            binding.fabPlay.setVisibility(View.GONE);
            return true;
        }
        return super.onBackPressed();
    }
}
