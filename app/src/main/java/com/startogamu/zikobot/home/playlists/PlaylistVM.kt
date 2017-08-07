package com.startogamu.zikobot.home.playlists

import android.content.Context
import android.databinding.Bindable
import android.view.View
import com.joxad.easydatabinding.base.BaseVM
import com.joxad.zikobot.data.db.model.ZikoPlaylist
import com.startogamu.zikobot.R

/**
 * Created by Jocelyn on 31/07/2017.
 */

class PlaylistVM(section: Boolean, context: Context, model: ZikoPlaylist) : BaseVM<ZikoPlaylist>(context, model) {

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

    @Bindable
    fun getIcon(): Int? {
        if (model.spotifyId != null)
            return R.drawable.ic_spotify_green
        return null
    }

    fun onClick(@SuppressWarnings("unused") v: View) {
        context.startActivity(PlaylistDetailActivity.newInstance(context, model.id))
    }

}
