package com.startogamu.zikobot.home.albums;

import android.content.Context;
import android.databinding.Bindable;

import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.data.db.model.ZikoAlbum;

/**
 * Created by Jocelyn on 31/07/2017.
 */

public class AlbumVM extends BaseVM<ZikoAlbum> {
    /***

     * @param context
     * @param model
     */
    public AlbumVM(Context context, ZikoAlbum model) {
        super(context, model);
    }

    @Override
    public void onCreate() {

    }

    @Bindable
    public String getName() {
        return model.getName();
    }
}
