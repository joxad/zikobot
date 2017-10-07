package com.startogamu.zikobot.home.track

import android.content.Context
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.databinding.ObservableBoolean
import android.support.v4.app.FragmentActivity
import android.view.View
import com.joxad.easydatabinding.base.BaseVM
import com.joxad.zikobot.data.db.CurrentPlaylistManager
import com.joxad.zikobot.data.db.PlaylistManager
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.startogamu.zikobot.NavigationManager
import com.wang.avi.AVLoadingIndicatorView


/**
 * Generated by generator-android-template
 * <!!!!!> You have to replace the Object to your POJO class that will be handled in the VM <!!!!!>
 */
class TrackVM
/***
 * @param context
 * *
 * @param model
 */
(context: Context, model: ZikoTrack) : BaseVM<ZikoTrack>(context, model) {

    lateinit var playing: ObservableBoolean
    override fun onCreate() {
        if (model.zikoAlbum != null)
            model.zikoAlbum.load()
        if (model.zikoArtist != null)
            model.zikoArtist.load()
        playing = ObservableBoolean(false)
        notifyChange()
    }

    val title: String
        @Bindable
        get() = model.name

    val image: String?
        @Bindable
        get() {
            if (model.zikoAlbum != null)
                return model.zikoAlbum.image
            return null
        }

    val artistName: String
        @Bindable
        get() {
            if (model.zikoArtist != null)
                return model.zikoArtist.name
            return "Unknown artist"
        }

    fun onClick(v: View) {
        CurrentPlaylistManager.INSTANCE.play(model)
    }

    fun onLongClick(v:View): Boolean {
        if (PlaylistManager.INSTANCE.hasData())
            NavigationManager.showAddToPlaylist(context as FragmentActivity)
        else
            NavigationManager.showCreatePlaylist(context as FragmentActivity)
        return true

    }

    companion object {
        private val TAG = TrackVM::class.java.simpleName

        @BindingAdapter("animating")
        fun animating(view: AVLoadingIndicatorView, animate: Boolean) {
            if (animate)
                view.smoothToShow()
            else {
                view.smoothToHide()
            }
        }
    }
}
