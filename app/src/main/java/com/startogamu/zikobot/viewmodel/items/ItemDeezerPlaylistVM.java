package com.startogamu.zikobot.viewmodel.items;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.deezer.sdk.model.Playlist;
import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.deezer.SelectDeezerItemPlaylistEvent;

import org.greenrobot.eventbus.EventBus;


/***
 * ViewModel that will represent the playlist of a user on spotify
 */
public class ItemDeezerPlaylistVM extends BaseVM<Playlist> {


    /***
     * @param context
     * @param model
     */
    public ItemDeezerPlaylistVM(Context context, Playlist model) {
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
        EventBus.getDefault().post(new SelectDeezerItemPlaylistEvent(model));
    }


    @Bindable
    public String getName() {
        return model.getTitle();
    }


    @Bindable
    public String getImageUrl() {
        return model.getBigImageUrl();
    }

    @Bindable
    public String getNbSongs() {
        return String.format(context.getString(R.string.playlist_total_tracks),
                model.getTracks().size());
    }


    @Override
    public void destroy() {

    }
}
