package com.startogamu.zikobot.home.albums

import android.os.Bundle
import android.view.View
import com.joxad.androidtemplate.core.log.AppLog
import com.joxad.zikobot.data.db.AlbumManager
import com.joxad.zikobot.data.db.CurrentPlaylistManager
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.startogamu.zikobot.ABasePlayerActivityVM
import com.startogamu.zikobot.Constants
import com.startogamu.zikobot.core.AppUtils
import com.startogamu.zikobot.databinding.AlbumDetailActivityBinding
import com.startogamu.zikobot.databinding.PlayerViewBottomBinding
import com.startogamu.zikobot.home.track.TrackVM

/**
 * Generated by generator-android-template
 */
class AlbumDetailActivityVM(activity: AlbumDetailActivity, binding: AlbumDetailActivityBinding, savedInstance: Bundle?) : ABasePlayerActivityVM<AlbumDetailActivity, AlbumDetailActivityBinding>(activity, binding, savedInstance) {
    override fun playAll(view: View?) {
        val list: List<ZikoTrack> = items.map { it.model }
        CurrentPlaylistManager.INSTANCE.play(list)
    }

    override fun playerBinding(): PlayerViewBottomBinding {
        return binding.viewPlayer!!
    }

    lateinit var albumVM: AlbumVM

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        loadData()

    }

    fun loadData() {
        val aId = activity.intent.getIntExtra(Constants.Extra.ALBUM_ID, 0)

        AlbumManager.findOne(aId)
                .querySingle()
                .subscribe({ it ->
                    albumVM = AlbumVM(false, activity, it)
                    AppUtils.animateAlpha(binding.fabPlay)
                    addTracks(0)

                }, {
                    AppLog.INSTANCE.e("Album", it.localizedMessage)
                })
    }

    private fun addTracks(indexStart: Int) {
        AlbumManager.findTracks(albumVM.model.id, indexStart)
                .subscribe({
                    for (zT in it) {
                        // items.add(TrackVM(activity, zT))
                    }
                }, {
                    AppLog.INSTANCE.e("Add track", it.localizedMessage)
                })

        AlbumManager.findTracksFromAPI(albumVM.model)
                .subscribe({
                    for (zT in it) {
                        items.add(TrackVM(activity, zT))
                    }
                }, {
                    AppLog.INSTANCE.e("Add track", it.localizedMessage)
                })
    }

}