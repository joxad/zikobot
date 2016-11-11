package com.startogamu.zikobot.deezer;

import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.deezer.sdk.model.Playlist;
import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventPlayListClicked;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentDeezerTracksBinding;
import com.startogamu.zikobot.core.module.deezer.DeezerManager;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.localtracks.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;


/***
 * {@link FragmentDeezerTracksVM} will call the apimanager to get the tracks of the playlist
 */
public abstract class FragmentDeezerTracksVM extends FragmentBaseVM<FragmentDeezerTracks, FragmentDeezerTracksBinding> {

    private static final String TAG = FragmentDeezerTracksVM.class.getSimpleName();
    public ObservableArrayList<TrackVM> items;

    public abstract ItemView getItemView();

    @Nullable
    Playlist playlist;

    public FragmentDeezerTracksVM(FragmentDeezerTracks fragment, FragmentDeezerTracksBinding binding) {
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
    private void loadTracks(Playlist playlist) {
        items.clear();
        DeezerManager.playlistTracks(playlist.getId()).subscribe(tracks -> {
            for (com.deezer.sdk.model.Track track : tracks) {
                items.add(new TrackVM(fragment.getContext(), Track.from(track)));
            }
        }, throwable -> {

        });
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        loadTracks(playlist);
    }


    @Subscribe
    public void onEvent(EventPlayListClicked eventPlayListClicked) {
        EventBus.getDefault().post(new EventAddTrackToPlayer(items));
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

}
