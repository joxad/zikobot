package com.startogamu.zikobot.album;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.LocalAlbumSelectEvent;
import com.startogamu.zikobot.core.event.dialog.EventShowDialogAlbumSettings;
import com.startogamu.zikobot.core.model.Album;
import com.startogamu.zikobot.core.module.localmusic.model.LocalAlbum;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 06/06/16.
 */
public class AlbumVM extends BaseVM<LocalAlbum> {

    /***
     * @param context
     * @param model
     */
    public AlbumVM(Context context, LocalAlbum model) {
        super(context, model);
    }

    @Override
    public void onCreate() {

    }

    public boolean onLongClick(View view) {
        EventBus.getDefault().post(new EventShowDialogAlbumSettings(model));
        return true;
    }

    public void onClick(View view) {
        //TODO sendevent
        EventBus.getDefault().post(new LocalAlbumSelectEvent(Album.from(model), view.findViewById(R.id.iv_album)));
    }

    @Bindable
    public String getName() {
        return model.getName();
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
