package com.startogamu.zikobot.album;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.LocalAlbumSelectEvent;
import com.startogamu.zikobot.module.localmusic.model.LocalAlbum;
import com.startogamu.zikobot.core.model.Album;

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
    public void init() {

    }

    @Override
    public void destroy() {

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
    public String getImageUrl() {
        return model.getImage();
    }
}
