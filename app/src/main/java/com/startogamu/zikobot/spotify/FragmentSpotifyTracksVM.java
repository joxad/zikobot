package com.startogamu.zikobot.spotify;

import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventPlayListClicked;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentSpotifyTracksBinding;

import com.startogamu.zikobot.core.module.mock.Mock;
import com.startogamu.zikobot.core.module.spotify_api.manager.SpotifyApiManager;
import com.startogamu.zikobot.core.module.spotify_api.model.Item;
import com.startogamu.zikobot.core.module.spotify_api.model.SpotifyPlaylistItem;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.localtracks.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;


/***
 * {@link FragmentSpotifyTracksVM} will call the apimanager to get the tracks of the playlist
 */
public abstract class FragmentSpotifyTracksVM extends FragmentBaseVM<FragmentSpotifyTracks, FragmentSpotifyTracksBinding> {

    private static final String TAG = FragmentSpotifyTracksVM.class.getSimpleName();
    public ObservableArrayList<TrackVM> items;

    public abstract ItemView getItemView();

    @Nullable
    Item playlist;

    public FragmentSpotifyTracksVM(FragmentSpotifyTracks fragment, FragmentSpotifyTracksBinding binding) {
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
    private void loadTracks(Item playlist) {
        items.clear();
        items.addAll(Mock.tracks(fragment.getContext(), playlist.tracks.getTotal()));
        SpotifyApiManager.getInstance().getPlaylistTracks(playlist.getId()).subscribe(spotifyPlaylistWithTrack -> {
            items.clear();
            for (SpotifyPlaylistItem playlistItem : spotifyPlaylistWithTrack.getItems()) {
                items.add(new TrackVM(fragment.getContext(), Track.from(playlistItem.track)));
            }
        }, throwable -> {
            Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
        });
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
