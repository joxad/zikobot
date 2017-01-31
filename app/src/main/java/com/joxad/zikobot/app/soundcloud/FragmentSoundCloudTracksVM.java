package com.joxad.zikobot.app.soundcloud;

import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.app.player.event.EventPlayListClicked;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.FragmentSoundCloudTracksBinding;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudTrack;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.app.localtracks.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;


/***
 * {@link FragmentSoundCloudTracksVM} will call the apimanager to get the tracks of the playlist
 */
public abstract class FragmentSoundCloudTracksVM extends FragmentBaseVM<FragmentSoundCloudTracks, FragmentSoundCloudTracksBinding> {

    private static final String TAG = FragmentSoundCloudTracksVM.class.getSimpleName();
    public ObservableArrayList<TrackVM> items;

    public abstract ItemView getItemView();

    @Nullable
    SoundCloudPlaylist playlist;

    public FragmentSoundCloudTracksVM(FragmentSoundCloudTracks fragment, FragmentSoundCloudTracksBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void onCreate() {
        Parcelable parcelable = fragment.getArguments().getParcelable(EXTRA.PLAYLIST);
        playlist = parcelable != null ? Parcels.unwrap(parcelable) : null;

        items = new ObservableArrayList<>();
    }

    /***
     * FInd the list of track from the playlist
     *
     * @param playlist
     */
    private void loadTracks(SoundCloudPlaylist playlist) {
        items.clear();
        for (SoundCloudTrack track : playlist.getSoundCloudTracks()) {
            items.add(new TrackVM(fragment.getContext(),
                    Track.from(track, fragment.getContext().getString(R.string.soundcloud_id))));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }


    @Subscribe
    public void onEvent(EventPlayListClicked eventPlayListClicked) {
        EventBus.getDefault().post(new EventAddList(items));
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

}
