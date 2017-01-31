package com.joxad.zikobot.app.spotify;

import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.app.player.event.EventPlayListClicked;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.app.core.mock.Mock;
import com.joxad.zikobot.data.module.spotify_api.model.Item;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.databinding.FragmentSpotifyTracksBinding;

import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyPlaylistItem;

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
