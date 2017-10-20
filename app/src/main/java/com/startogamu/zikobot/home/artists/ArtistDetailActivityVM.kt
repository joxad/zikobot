package com.startogamu.zikobot.home.artists

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.joxad.androidtemplate.core.log.AppLog
import com.joxad.androidtemplate.core.view.utils.EndlessRecyclerOnScrollListener
import com.joxad.zikobot.data.db.ArtistManager
import com.joxad.zikobot.data.db.CurrentPlaylistManager
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.startogamu.zikobot.ABasePlayerActivityVM
import com.startogamu.zikobot.BR
import com.startogamu.zikobot.Constants
import com.startogamu.zikobot.R
import com.startogamu.zikobot.core.AppUtils
import com.startogamu.zikobot.databinding.ArtistDetailActivityBinding
import com.startogamu.zikobot.databinding.PlayerViewBottomBinding
import com.startogamu.zikobot.home.track.TrackVM
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ArtistDetailActivityVM(activity: ArtistDetailActivity, binding: ArtistDetailActivityBinding, savedInstance: Bundle?) : ABasePlayerActivityVM<ArtistDetailActivity, ArtistDetailActivityBinding>(activity, binding, savedInstance) {

    override fun playAll(view: View?) {
        val list: List<ZikoTrack> = items.map { it.model }
        CurrentPlaylistManager.INSTANCE.play(list)
    }

    override fun playerBinding(): PlayerViewBottomBinding {
        return binding.viewPlayer!!
    }

    val itemView: ItemBinding<TrackVM> = ItemBinding.of<TrackVM>(BR.trackVM, R.layout.track_item)
    lateinit var artistVM: ArtistVM
    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        items = ObservableArrayList()
        AppUtils.initToolbar(activity, binding.toolbarDetailActivity!!)
        loadData()
        binding.artistDetailActivityRv.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                addTracks(page)
            }
        })
    }

    /***
     * Load the data from WS or DB to show them in the recycler
     */
    private fun loadData() {
        val aId = activity.intent.getIntExtra(Constants.Extra.ARTIST_ID, 0)

        ArtistManager.findOne(aId)
                .subscribe { it ->
                    artistVM = ArtistVM(activity, it)
                    AppUtils.animateAlpha(binding.fabPlay)
                }
        addTracks(0)
    }

    private fun addTracks(indexStart: Int) {
        ArtistManager.findTracks(artistVM.model.id, indexStart)
                .subscribe({
                    for (zT in it) {
                        if (zT.zikoPlaylist.id != CurrentPlaylistManager.INSTANCE.CURRENT)
                            items.add(TrackVM(activity, zT))
                    }
                }, {
                    AppLog.INSTANCE.e("Add track", it.localizedMessage)
                })
    }

    companion object {
        private val TAG = ArtistDetailActivityVM::class.java.simpleName
    }


}
