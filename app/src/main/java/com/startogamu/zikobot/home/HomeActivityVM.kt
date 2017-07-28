package com.startogamu.zikobot.home

import android.os.Bundle
import com.joxad.androidtemplate.core.adapter.GenericFragmentAdapter
import com.joxad.androidtemplate.core.fragment.FragmentTab

import com.joxad.easydatabinding.activity.ActivityBaseVM
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager
import com.startogamu.zikobot.R
import com.startogamu.zikobot.databinding.HomeActivityBinding
import com.startogamu.zikobot.home.playlists.PlaylistsFragment
import com.tbruyelle.rxpermissions.RxPermissions

/**
 * Created by Jocelyn on 27/07/2017.
 */

class HomeActivityVM(activity: HomeActivity?, binding: HomeActivityBinding?, savedInstance: Bundle?) :
        ActivityBaseVM<HomeActivity, HomeActivityBinding>(activity, binding, savedInstance) {


    override fun onCreate(savedInstance: Bundle?) {
        activity.setSupportActionBar(binding.toolbar)

        val genericFragmentAdapter = object : GenericFragmentAdapter(activity) {
            override fun tabTitles(): List<FragmentTab>? {
                return arrayListOf(
                        FragmentTab(activity?.getString(R.string.drawer_my_playlists), PlaylistsFragment.newInstance()),
                        FragmentTab(activity?.getString(R.string.drawer_filter_artiste), PlaylistsFragment.newInstance())
                )
            }
        }
        binding.viewPager.adapter = genericFragmentAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val rxPermission = RxPermissions(activity)
        rxPermission.request(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe({
                    granted ->
                    if (granted)
                        LocalMusicManager.INSTANCE.syncLocalData()
                })
    }


}
