package com.startogamu.zikobot.home.albums

import android.app.Activity
import android.content.Context
import android.databinding.Bindable
import android.view.View
import com.joxad.androidtemplate.core.log.AppLog

import com.joxad.easydatabinding.base.BaseVM
import com.joxad.zikobot.data.db.model.ZikoAlbum
import com.joxad.zikobot.data.module.lastfm.LastFmManager
import com.startogamu.zikobot.BR
import com.startogamu.zikobot.NavigationManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Jocelyn on 31/07/2017.
 */

class AlbumVM
/***

 * @param context
 * *
 * @param model
 */
(section : Boolean, context: Context, model: ZikoAlbum) : BaseVM<ZikoAlbum>(context, model) {


    override fun onCreate() {
        if (getImage().isNullOrEmpty()) {
            LastFmManager.INSTANCE.findAlbum("${getName()} ${getArtist()}")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        album ->
                        model.image = album.image[2].text
                        model.save()
                        notifyPropertyChanged(BR.image)
                    }, {
                        t ->
                        AppLog.INSTANCE.e("Album", t.localizedMessage)
                    })
        }

    }

    private fun getArtist(): String? {
        return model?.artist?.name
    }

    @Bindable
    fun getName(): String {
        return model.name
    }

    @Bindable
    fun getImage(): String? {
        return model.image
    }

    fun onClick(view: View) {
        NavigationManager.goToAlbum(context as Activity, model, view)
    }
}
