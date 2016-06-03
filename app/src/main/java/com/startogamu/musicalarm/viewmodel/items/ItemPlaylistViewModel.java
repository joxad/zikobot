package com.startogamu.musicalarm.viewmodel.items;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.IVM;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.event.SelectItemPlaylistEvent;
import com.startogamu.musicalarm.module.spotify_api.model.Item;
import com.startogamu.musicalarm.view.fragment.SpotifyMusicFragment;

import org.greenrobot.eventbus.EventBus;


/***
 * ViewModel that will represent the playlist of a user on spotify
 */
public class ItemPlaylistViewModel extends BaseObservable implements IVM {

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


    @Override
    public void init() {

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
        EventBus.getDefault().post(new SelectItemPlaylistEvent(item));
    }


    @Bindable
    public String getName() {
        return item.getName();
    }


    @Bindable
    public String getImageUrl() {
        if (item.getImages() != null && item.getImages().size() > 0)
            return item.getImages().get(0).getUrl();
        return "";
    }

    @Bindable
    public String getNbSongs() {
        return String.format(fragment.getString(R.string.playlist_total_tracks),
                item.tracks.total);
    }


    @Override
    public void destroy() {

    }
}
