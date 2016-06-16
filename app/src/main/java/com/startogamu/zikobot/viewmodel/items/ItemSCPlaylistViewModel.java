package com.startogamu.zikobot.viewmodel.items;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.soundcloud.SelectSCItemPlaylistEvent;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudPlaylist;

import org.greenrobot.eventbus.EventBus;


/***
 * ViewModel that will represent the playlist of a user on spotify
 */
public class ItemSCPlaylistViewModel extends BaseVM<SoundCloudPlaylist> {


    /***
     * @param context
     * @param model
     */
    public ItemSCPlaylistViewModel(Context context, SoundCloudPlaylist model) {
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
        EventBus.getDefault().post(new SelectSCItemPlaylistEvent(model));
    }


    @Bindable
    public String getName() {
        return model.getTitle();
    }


    @Bindable
    public String getImageUrl() {
        return model.getArtworkUrl();
    }

    @Bindable
    public String getNbSongs() {
        return String.format(context.getString(R.string.playlist_total_tracks),
                model.getTrackCount());
    }


    @Override
    public void destroy() {

    }
}
