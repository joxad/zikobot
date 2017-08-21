package com.startogamu.zikobot.home.playlists

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.view.View
import com.joxad.zikobot.data.db.CurrentPlaylistManager
import com.joxad.zikobot.data.db.PlaylistManager
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.startogamu.zikobot.ABasePlayerActivityVM
import com.startogamu.zikobot.BR
import com.startogamu.zikobot.Constants
import com.startogamu.zikobot.R
import com.startogamu.zikobot.core.AppUtils
import com.startogamu.zikobot.databinding.PlayerViewBottomBinding
import com.startogamu.zikobot.databinding.PlaylistDetailActivityBinding
import com.startogamu.zikobot.home.track.TrackVM
import me.tatarka.bindingcollectionadapter2.ItemBinding


/**
 * Generated by generator-android-template
 */
class PlaylistDetailActivityVM(activity: PlaylistDetailActivity, binding: PlaylistDetailActivityBinding, savedInstance: Bundle?) : ABasePlayerActivityVM<PlaylistDetailActivity, PlaylistDetailActivityBinding>(activity, binding, savedInstance) {
    override fun playAll(view: View?) {
        val list: List<ZikoTrack> = items.map { it.model }
        CurrentPlaylistManager.INSTANCE.play(list)
    }

    fun prepareAlarm(view: View?) {

    }

    override fun playerBinding(): PlayerViewBottomBinding {
        return binding.viewPlayer!!
    }

    lateinit var items: ObservableArrayList<TrackVM>
    val itemBinding: ItemBinding<TrackVM> = ItemBinding.of<TrackVM>(BR.trackVM, R.layout.track_item)
    lateinit var playlistVM: PlaylistVM

    override fun onCreate(saved: Bundle?) {
        super.onCreate(saved)
        items = ObservableArrayList()
        AppUtils.initToolbar(activity, binding.toolbarDetailActivity!!)
        loadData()
    }


    /***
     * Load the data spotify WS or DB to show them in the recycler
     */
    private fun loadData() {
        val pId = activity.intent.getLongExtra(Constants.Extra.PLAYLIST_ID, 0)
        PlaylistManager.INSTANCE.findOne(pId)
                .querySingle().subscribe({ zi ->
            playlistVM = PlaylistVM(false, activity, zi)
            for (track in playlistVM.model.getForeignTracks()) {
                items.add(TrackVM(activity, track))
            }
            AppUtils.initFab(binding.fabPlay)
            AppUtils.initFab(binding.fabAlarm)
        })

    }

    override fun onBackPressed(): Boolean {
        if (playerVM.onBackPressed()) {
            binding.fabPlay.visibility = View.GONE
            binding.fabAlarm.visibility = View.GONE
            return true
        }
        return false
    }

    companion object {

        private val TAG = PlaylistDetailActivityVM::class.java.simpleName
    }


}
