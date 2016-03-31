package com.startogamu.musicalarm.viewmodel.items;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.model.spotify.Item;
import com.startogamu.musicalarm.utils.EXTRA;
import com.startogamu.musicalarm.viewmodel.ViewModel;

/***
 * ViewModel that will represent the playlist of a user on spotify
 */
public class ItemPlaylistViewModel extends BaseObservable implements ViewModel {

    private Item item;
    private Activity context;

    /***
     * @param context
     * @param item
     */
    public ItemPlaylistViewModel(Activity context, Item item) {
        this.item = item;
        this.context = context;
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
        Intent intent = new Intent();
        intent.putExtra(EXTRA.PLAYLIST_ID, item.getId());
        context.setResult(Activity.RESULT_OK, intent);
        context.finish();
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
        return String.format(context.getString(R.string.playlist_total_tracks),
                item.tracks.total);
    }

    @Override
    public void onDestroy() {

    }
}
