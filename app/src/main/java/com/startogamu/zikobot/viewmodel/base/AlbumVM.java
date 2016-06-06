package com.startogamu.zikobot.viewmodel.base;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalArtist;

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

    public void onClick(View view){
        //TODO sendevent
    }

    @Bindable
    public String getName() {
        return model.getName();
    }


    @Bindable
    public String getNbTracks() {
        return String.format("%d %s", model.getNbTracks(), context.getString(R.string.album));
    }
}
