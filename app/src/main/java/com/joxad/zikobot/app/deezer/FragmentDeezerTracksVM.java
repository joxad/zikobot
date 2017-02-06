package com.joxad.zikobot.app.deezer;

import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.deezer.sdk.model.Playlist;
import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.app.player.event.EventPlayListClicked;
import com.joxad.zikobot.data.module.deezer.DeezerManager;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.databinding.FragmentDeezerTracksBinding;
import com.joxad.zikobot.data.model.Track;

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
        DeezerManager.getInstance().playlistTracks(playlist.getId()).subscribe(tracks -> {
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
        EventBus.getDefault().post(new EventAddList(items));
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

}