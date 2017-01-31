package com.joxad.zikobot.app.deezer;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.deezer.sdk.model.Playlist;
import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.app.deezer.event.SelectDeezerItemPlaylistEvent;
import com.joxad.zikobot.app.R;

import org.greenrobot.eventbus.EventBus;


/***
 * ViewModel that will represent the playlist of a user on spotify
 */
public class DeezerPlaylistVM extends BaseVM<Playlist> {


    /***
     * @param context
     * @param model
     */
    public DeezerPlaylistVM(Context context, Playlist model) {
        super(context, model);
    }



    @Override
    public void onCreate() {

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
        return model.getMediumImageUrl();
    }

    @Bindable
    public String getNbSongs() {
        return String.format(context.getString(R.string.playlist_total_tracks),
                model.getTracks().size());
    }

}
