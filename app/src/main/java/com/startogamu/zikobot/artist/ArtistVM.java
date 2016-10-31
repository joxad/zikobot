package com.startogamu.zikobot.artist;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventShowArtistDetail;
import com.startogamu.zikobot.core.model.Artist;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 06/06/16.
 */
public class ArtistVM extends BaseVM<Artist> {

    /***
     * @param context
     * @param model
     */
    public ArtistVM(Context context, Artist model) {
        super(context, model);
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    public void onClick(View view){

        EventBus.getDefault().post(new EventShowArtistDetail(model, view.findViewById(R.id.iv_artist)));
    }

    @Bindable
    public String getName() {
        return model.getName();
    }


    @Bindable
    public String getNbAlbums() {
        return "";//String.format("%d %s", model.getNbAlbums(), context.getString(R.string.album));
    }

    @Bindable
    public String getImageUrl() {
        return model.getImage();
    }
}
