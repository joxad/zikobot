package com.startogamu.musicalarm.viewmodel.items;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.event.SelectItemPlaylistEvent;
import com.startogamu.musicalarm.module.spotify_api.model.Item;

import org.greenrobot.eventbus.EventBus;


/***
 * ViewModel that will represent the playlist of a user on spotify
 */
public class ItemPlaylistViewModel extends BaseVM<Item> {


    /***
     * @param context
     * @param model
     */
    public ItemPlaylistViewModel(Context context, Item model) {
        super(context, model);
    }

    @Override
    public void init() {

    }

    public void setItem(Item item) {
        this.model = item;
        notifyChange();
    }

    /***
     * Click event of an item of alarm
     *
     * @param view
     */
    public void onItemClick(View view) {
        EventBus.getDefault().post(new SelectItemPlaylistEvent(model));
    }


    @Bindable
    public String getName() {
        return model.getName();
    }


    @Bindable
    public String getImageUrl() {
        if (model.getImages() != null && model.getImages().size() > 0)
            return model.getImages().get(0).getUrl();
        return "";
    }

    @Bindable
    public String getNbSongs() {
        return String.format(context.getString(R.string.playlist_total_tracks),
                model.tracks.total);
    }


    @Override
    public void destroy() {

    }
}
