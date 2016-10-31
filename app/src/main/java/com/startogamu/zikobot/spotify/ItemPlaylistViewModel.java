package com.startogamu.zikobot.spotify;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.SelectItemPlaylistEvent;
import com.startogamu.zikobot.module.spotify_api.model.Item;

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

    /***
     * Click event of an item of alarm
     *
     * @param view
     */
    public void onItemClick(View view) {
        if (model.getId() != null)
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
