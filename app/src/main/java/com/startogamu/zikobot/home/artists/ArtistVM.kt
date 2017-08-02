package com.startogamu.zikobot.home.artists

import android.content.Context
import android.databinding.Bindable

import com.joxad.easydatabinding.base.BaseVM
import com.joxad.zikobot.data.db.model.ZikoArtist

/**
 * Generated by generator-android-template
 * <!!!!!> You have to replace the Object to your POJO class that will be handled in the VM <!!!!!>
 */
class ArtistVM(context: Context, model: ZikoArtist) : BaseVM<ZikoArtist>(context, model) {

    override fun onCreate() {

    }

    @Bindable
    fun getName(): String {
        return model.name
    }

    @Bindable
    fun getImage(): String {
        return model.image
    }
}
