package com.startogamu.zikobot.soundcloud;

import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventPlayListClicked;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentSoundCloudTracksBinding;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudTrack;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.localtracks.TrackVM;

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
    public void init() {
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
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }


    @Subscribe
    public void onEvent(EventPlayListClicked eventPlayListClicked) {
        EventBus.getDefault().post(new EventAddTrackToPlayer(items));
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void destroy() {
    }
}
