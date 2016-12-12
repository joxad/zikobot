package com.startogamu.zikobot.soundcloud;

import android.databinding.ObservableArrayList;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.dialog.EventShowDialogAlarm;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.databinding.ActivitySoundCloudBinding;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudTrack;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.alarm.DialogFragmentAlarms;
import com.startogamu.zikobot.localtracks.TrackVM;
import com.startogamu.zikobot.player.PlayerVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 09/08/16.
 */
public class ActivitySoundCloudVM extends ActivityBaseVM<ActivitySoundCloud, ActivitySoundCloudBinding> {


    private static final String TAG = ActivitySoundCloudVM.class.getSimpleName();
    public ItemView itemViewTrack = ItemView.of(BR.trackVM, R.layout.item_track);

    public PlayerVM playerVM;
    public ObservableArrayList<TrackVM> tracks;

    public SoundCloudPlaylist soundCloudPlaylist;

    /***
     * @param activity
     * @param binding
     */
    public ActivitySoundCloudVM(ActivitySoundCloud activity, ActivitySoundCloudBinding binding) {
        super(activity, binding);
    }

    @Override
    public void onCreate() {
        soundCloudPlaylist = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.PLAYLIST));
        tracks = new ObservableArrayList<>();
        binding.rv.setNestedScrollingEnabled(false);
        initToolbar();
        initPlayerVM();
    }


    /***
     * Init the toolar
     */
    private void initToolbar() {
        ZikoUtils.prepareToolbar(activity, binding.customToolbar, soundCloudPlaylist.getTitle(), soundCloudPlaylist.getSoundCloudTracks().get(0).getArtworkUrl());
        ZikoUtils.animateScale(binding.fabPlay);
    }


    /***
     *
     */

    private void initPlayerVM() {
        playerVM = new PlayerVM(activity);
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
    public void onEvent(EventShowDialogAlarm event) {
        DialogFragmentAlarms dialogFragmentAlarms = DialogFragmentAlarms.newInstance(event.getModel());
        dialogFragmentAlarms.show(activity.getSupportFragmentManager(), DialogFragmentAlarms.TAG);

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
        EventBus.getDefault().post(new EventAddTrackToPlayer(tracks));
    }

    @Override
    protected boolean onBackPressed() {

        return super.onBackPressed();
    }
}
