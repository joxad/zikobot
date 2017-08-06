package com.startogamu.zikobot.home.playlists

import android.content.Context
import android.databinding.Bindable
import com.joxad.easydatabinding.base.BaseVM
import com.joxad.zikobot.data.db.model.ZikoPlaylist

/**
 * Created by Jocelyn on 31/07/2017.
 */

public class PlaylistVM(section: Boolean, context: Context, model: ZikoPlaylist) : BaseVM<ZikoPlaylist>(context, model) {

    override fun onCreate() {

    }

    @Bindable
    fun getName(): String? {
        return model.name
    }

    @Bindable
    fun getImage(): String? {
        return model.imageUrl
    }
}
