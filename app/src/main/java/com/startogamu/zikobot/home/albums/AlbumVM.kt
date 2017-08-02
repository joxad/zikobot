package com.startogamu.zikobot.home.albums

import android.content.Context
import android.databinding.Bindable

import com.joxad.easydatabinding.base.BaseVM
import com.joxad.zikobot.data.db.model.ZikoAlbum

/**
 * Created by Jocelyn on 31/07/2017.
 */

class AlbumVM
/***

 * @param context
 * *
 * @param model
 */
(context: Context, model: ZikoAlbum) : BaseVM<ZikoAlbum>(context, model) {

    override fun onCreate() {

    }

    val name: String
        @Bindable
        get() = model.name
}
