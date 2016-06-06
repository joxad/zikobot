package com.startogamu.zikobot.viewmodel.base;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.LocalArtistSelectEvent;
import com.startogamu.zikobot.module.content_resolver.model.LocalArtist;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 06/06/16.
 */
public class ArtistVM extends BaseVM<LocalArtist> {

    /***
     * @param context
     * @param model
     */
    public ArtistVM(Context context, LocalArtist model) {
        super(context, model);
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    public void onClick(View view){
        EventBus.getDefault().post(new LocalArtistSelectEvent(model));
    }

    @Bindable
    public String getName() {
        return model.getName();
    }


    @Bindable
    public String getNbAlbums() {
        return String.format("%d %s", model.getNbAlbums(), context.getString(R.string.album));
    }
}
