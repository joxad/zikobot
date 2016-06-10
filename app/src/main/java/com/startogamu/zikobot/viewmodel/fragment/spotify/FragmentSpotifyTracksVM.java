package com.startogamu.zikobot.viewmodel.fragment.spotify;

import android.databinding.ObservableArrayList;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.SelectAllTracks;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentSpotifyTracksBinding;
import com.startogamu.zikobot.module.alarm.model.AlarmTrack;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.mock.Mock;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyPlaylistItem;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyTracks;
import com.startogamu.zikobot.viewmodel.base.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.tatarka.bindingcollectionadapter.ItemView;


/***
 * {@link FragmentSpotifyTracksVM} will call the apimanager to get the tracks of the playlist
 */
public abstract class FragmentSpotifyTracksVM extends FragmentBaseVM<FragmentSpotifyTracks, FragmentSpotifyTracksBinding> {

    public ObservableArrayList<TrackVM> items;


    public abstract ItemView getItemView();


    @Nullable
    @InjectExtra(EXTRA.PLAYLIST_ID)
    String playlistId;
    @Nullable
    @InjectExtra(EXTRA.PLAYLIST_TRACKS_TOTAL)
    int tracksNumber = 0;

    public FragmentSpotifyTracksVM(FragmentSpotifyTracks fragment, FragmentSpotifyTracksBinding binding) {
        super(fragment, binding);
        Dart.inject(this, fragment.getArguments());
        this.items = new ObservableArrayList<>();
        this.fragment = fragment;
        this.binding = binding;
        EventBus.getDefault().register(this);
        Injector.INSTANCE.spotifyApi().inject(this);

        loadTracks(playlistId);
    }

    @Override
    public void init() {
    }

    /***
     * FInd the list of track from the playlist
     *
     * @param playlistId
     */
    private void loadTracks(String playlistId) {
        items.clear();
        items.addAll(Mock.tracks(fragment.getContext(), tracksNumber));
        Injector.INSTANCE.spotifyApi().manager().getPlaylistTracks(playlistId).subscribe(spotifyPlaylistWithTrack -> {
            items.clear();
            for (SpotifyPlaylistItem playlistItem : spotifyPlaylistWithTrack.getItems()) {
                items.add(new TrackVM(fragment.getContext(), AlarmTrack.from(playlistItem.track)));
            }
        }, throwable -> {
            Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
        });
    }

    @Subscribe
    public void onEvent(SelectAllTracks selectAllTracks) {
        for (TrackVM trackVM : items) {
            trackVM.select();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void destroy() {
    }
}
