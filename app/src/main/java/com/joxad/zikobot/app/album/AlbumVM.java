package com.joxad.zikobot.app.album;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.data.event.LocalAlbumSelectEvent;
import com.joxad.zikobot.data.event.dialog.EventShowDialogAlbumSettings;
import com.joxad.zikobot.data.db.model.Album;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 06/06/16.
 */
public class AlbumVM extends BaseVM<Album> {

    /***
     * @param context
     * @param model
     */
    public AlbumVM(Context context, Album model) {
        super(context, model);
    }

    @Override
    public void onCreate() {

    }

    public void onMoreClicked(View view) {
        onLongClick(view);
    }

    public boolean onLongClick(View view) {
        EventBus.getDefault().post(new EventShowDialogAlbumSettings(model));
        return true;
    }

    public void onClick(View view) {
        //TODO sendevent
        EventBus.getDefault().post(new LocalAlbumSelectEvent(model, view.findViewById(R.id.iv_album)));
    }

    @Bindable
    public String getName() {
        return model.getName();
    }


    @Bindable
    public int getNbTracksNumber() {
        return model.getNbTracks();
    }

    @Bindable
    public String getNbTracks() {
        return String.format("%d %s", model.getNbTracks(), context.getString(R.string.tracks));
    }

    @Bindable
    public String getArtist() {
        return model.getArtist();
    }

    @Bindable
    public String getImageUrl() {
        return model.getImage();
    }

    @Bindable
    public String getTransition() {
        return context.getString(R.string.transition) + model.getId();
    }

}
