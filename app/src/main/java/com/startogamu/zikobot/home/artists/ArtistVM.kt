package com.startogamu.zikobot.home.artists

import android.app.Activity
import android.content.Context
import android.databinding.Bindable
import android.view.View
import com.joxad.androidtemplate.core.log.AppLog

import com.joxad.easydatabinding.base.BaseVM
import com.joxad.zikobot.data.db.model.ZikoArtist
import com.joxad.zikobot.data.module.lastfm.LastFmManager
import com.startogamu.zikobot.BR
import com.startogamu.zikobot.NavigationManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Generated by generator-android-template
 * <!!!!!> You have to replace the Object to your POJO class that will be handled in the VM <!!!!!>
 */
class ArtistVM(context: Context, model: ZikoArtist) : BaseVM<ZikoArtist>(context, model) {

    override fun onCreate() {
        if (getImage().isNullOrEmpty()) {
            LastFmManager.INSTANCE.findArtist(getName())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        artist ->
                        model.image = artist.image[0].text
                        model.save()
                        notifyPropertyChanged(BR.image)
                    }, {
                        t ->
                        AppLog.INSTANCE.e("Artist", t.localizedMessage)
                    })
        }
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
        NavigationManager.goToArtist(context as Activity, model, view)
    }
}
