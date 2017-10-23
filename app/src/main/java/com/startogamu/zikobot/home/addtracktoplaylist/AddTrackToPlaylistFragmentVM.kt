package com.startogamu.zikobot.home.addtracktoplaylist

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.view.View
import com.joxad.androidtemplate.core.log.AppLog
import com.joxad.easydatabinding.bottomsheet.DialogBottomSheetBaseVM
import com.joxad.zikobot.data.db.PlaylistManager
import com.joxad.zikobot.data.db.TrackManager
import com.raizlabs.android.dbflow.rx2.kotlinextensions.list
import com.startogamu.zikobot.BR
import com.startogamu.zikobot.Constants
import com.startogamu.zikobot.R
import com.startogamu.zikobot.databinding.AddTrackToPlaylistFragmentBinding
import com.startogamu.zikobot.home.playlists.PlaylistVM
import com.startogamu.zikobot.home.track.TrackVM
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * Generated by generator-android-template
 */
class AddTrackToPlaylistFragmentVM(fragment: AddTrackToPlaylistFragment, binding: AddTrackToPlaylistFragmentBinding) : DialogBottomSheetBaseVM<AddTrackToPlaylistFragment, AddTrackToPlaylistFragmentBinding>(fragment, binding) {


    var itemBinding = ItemBinding.of<PlaylistVM>(BR.playlistVM, R.layout.playlist_item)
    lateinit var items: ObservableArrayList<PlaylistVM>
    lateinit var loading: ObservableBoolean
    lateinit var trackVM: TrackVM

    override fun onCreate() {
        items = ObservableArrayList()
        loading = ObservableBoolean(false)
        TrackManager.findOne(fragment.arguments.getLong(Constants.Extra.TRACK_ID))
                .querySingle()
                .subscribe({
                    trackVM = TrackVM(fragment.context, it)
                }, {
                    AppLog.INSTANCE.d(AddTrackToPlaylistFragmentVM::javaClass.name, it.localizedMessage)
                })
        loadData()
    }

    private fun loadData() {
        loading.set(true)
        PlaylistManager.INSTANCE.findAll().list {
            for (zikoP in it) {
                items.add(object : PlaylistVM(false, fragment.activity, zikoP) {
                    override fun onClick(v: View) {
                        PlaylistManager.INSTANCE.addTrack(model, trackVM.model)
                        fragment.dismiss()
                    }
                })
            }
            loading.set(false)
        }
    }

    override fun onCreate(savedInstance: Bundle?) {

    }

    companion object {

        val TAG = AddTrackToPlaylistFragmentVM::class.java.simpleName
    }
}