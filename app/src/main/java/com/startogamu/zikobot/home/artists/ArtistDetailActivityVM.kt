package com.startogamu.zikobot.home.artists

import android.os.Bundle
import android.view.View
import com.joxad.androidtemplate.core.adapter.GenericFragmentAdapter
import com.joxad.androidtemplate.core.fragment.FragmentTab
import com.joxad.zikobot.data.db.ArtistManager
import com.joxad.zikobot.data.db.CurrentPlaylistManager
import com.joxad.zikobot.data.db.model.ZikoArtist
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.startogamu.zikobot.ABasePlayerActivityVM
import com.startogamu.zikobot.Constants
import com.startogamu.zikobot.core.AppUtils
import com.startogamu.zikobot.databinding.ArtistDetailActivityBinding
import com.startogamu.zikobot.databinding.PlayerViewBottomBinding

class ArtistDetailActivityVM(activity: ArtistDetailActivity, binding: ArtistDetailActivityBinding, savedInstance: Bundle?) : ABasePlayerActivityVM<ArtistDetailActivity, ArtistDetailActivityBinding>(activity, binding, savedInstance) {

    override fun playAll(view: View?) {
        val list: List<ZikoTrack> = items.map { it.model }
        CurrentPlaylistManager.INSTANCE.play(list)
    }

    override fun playerBinding(): PlayerViewBottomBinding {
        return binding.viewPlayer!!
    }

    lateinit var artistVM: ArtistVM
    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        AppUtils.initToolbar(activity, binding.toolbarDetailActivity!!)
        binding.toolbarDetailActivity!!.tabLayout.visibility= View.VISIBLE
        loadData()
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
                    initTabs(it)
                }
    }

    private fun initTabs(it: ZikoArtist?) {

        val genericFragmentAdapter = object : GenericFragmentAdapter(activity) {
            override fun tabTitles(): List<FragmentTab>? {
                return arrayListOf(
                        FragmentTab("Général", ArtistHomeFragment.newInstance()),
                        FragmentTab("Albums", AlbumsArtistFragment.newInstance()),
                        FragmentTab("Mes Favoris", TracksArtistFragment.newInstance(it!!.id)),
                        FragmentTab("Similaires", SimilarArtistsFragment.newInstance(it!!.id))
                )
            }
        }
        binding.artistDetailViewPager.adapter = genericFragmentAdapter
        binding.toolbarDetailActivity!!.tabLayout.setupWithViewPager(binding.artistDetailViewPager)
    }


    companion object {
        private val TAG = ArtistDetailActivityVM::class.java.simpleName
    }


}
