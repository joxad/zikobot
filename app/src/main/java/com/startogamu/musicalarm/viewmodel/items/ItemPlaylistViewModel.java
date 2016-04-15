package com.startogamu.musicalarm.viewmodel.items;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.f2prateek.dart.henson.Bundler;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.module.spotify_api.object.Item;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.view.fragment.SpotifyMusicFragment;
import com.startogamu.musicalarm.view.fragment.SpotifyPlaylistTracksFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;


/***
 * ViewModel that will represent the playlist of a user on spotify
 */
public class ItemPlaylistViewModel extends BaseObservable implements ViewModel {

    private Item item;
    private SpotifyMusicFragment fragment;

    /***
     * @param fragment
     * @param item
     */
    public ItemPlaylistViewModel(SpotifyMusicFragment fragment, Item item) {
        this.item = item;
        this.fragment = fragment;
    }

    public void setItem(Item item) {
        this.item = item;
        notifyChange();
    }

    /***
     * Click event of an item of alarm
     *
     * @param view
     */
    public void onItemClick(View view) {
        fragment.replaceFragment(SpotifyPlaylistTracksFragment.newInstance
                (Bundler.create().put(EXTRA.PLAYLIST_ID, item.getId()).get()),true);

    }



    @Bindable
    public String getName() {
        return item.getName();
    }


    @Bindable
    public String getImageUrl() {
        return item.getImages().get(0).getUrl();
    }

    @Bindable
    public String getNbSongs() {
        return String.format(fragment.getString(R.string.playlist_total_tracks),
                item.tracks.total);
    }

    @Override
    public void onDestroy() {

    }
}
