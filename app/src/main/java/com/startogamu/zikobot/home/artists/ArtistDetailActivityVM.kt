package com.startogamu.zikobot.home.artists

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.view.View

import com.joxad.easydatabinding.activity.ActivityBaseVM
import com.joxad.zikobot.data.db.ArtistManager
import com.joxad.zikobot.data.db.CurrentPlaylistManager
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.startogamu.zikobot.ABasePlayerActivityVM
import com.startogamu.zikobot.BR
import com.startogamu.zikobot.Constants
import com.startogamu.zikobot.R
import com.startogamu.zikobot.databinding.ArtistDetailActivityBinding
import com.startogamu.zikobot.databinding.PlayerViewBottomBinding
import com.startogamu.zikobot.home.track.TrackVM

import me.tatarka.bindingcollectionadapter2.ItemBinding

class ArtistDetailActivityVM(activity: ArtistDetailActivity, binding: ArtistDetailActivityBinding, savedInstance: Bundle?) : ABasePlayerActivityVM<ArtistDetailActivity, ArtistDetailActivityBinding>(activity, binding, savedInstance) {
    override fun playAll(view: View?) {
        val list : List<ZikoTrack> = items.map { it.model }
        CurrentPlaylistManager.INSTANCE.play(list)
    }

    override fun playerBinding(): PlayerViewBottomBinding {
        return binding.viewPlayer!!
    }

    lateinit var items: ObservableArrayList<TrackVM>
    val itemView: ItemBinding<TrackVM> = ItemBinding.of<TrackVM>(BR.trackVM, R.layout.track_item)
    lateinit var artistVM: ArtistVM
    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        items = ObservableArrayList<TrackVM>()
        loadData()
    }

    /***
     * Load the data from WS or DB to show them in the recycler
     */
    fun loadData() {
        val aId = activity.intent.getIntExtra(Constants.Extra.ARTIST_ID, 0)

        ArtistManager.findOne(aId)
                .querySingle().subscribe({ zi ->
            artistVM = ArtistVM(activity, zi)
            for (track in artistVM.model.getForeignTracks()) {
                items.add(TrackVM(activity, track))
            }

        })
    }

    companion object {
        private val TAG = ArtistDetailActivityVM::class.java.simpleName
    }


}
