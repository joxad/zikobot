package com.startogamu.zikobot.home

import android.content.Intent
import android.os.Bundle
import com.joxad.androidtemplate.core.adapter.GenericFragmentAdapter
import com.joxad.androidtemplate.core.fragment.FragmentTab
import com.joxad.androidtemplate.core.log.AppLog
import com.joxad.easydatabinding.activity.ActivityBaseVM
import com.joxad.easydatabinding.activity.IResult
import com.joxad.zikobot.data.AppPrefs
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE
import com.startogamu.zikobot.R
import com.startogamu.zikobot.databinding.HomeActivityBinding
import com.startogamu.zikobot.ftu.AccountLinkFragment
import com.startogamu.zikobot.home.albums.AlbumsFragment
import com.startogamu.zikobot.home.artists.ArtistsFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by Jocelyn on 27/07/2017.
 */

class HomeActivityVM(activity: HomeActivity?, binding: HomeActivityBinding?, savedInstance: Bundle?) :
        ActivityBaseVM<HomeActivity, HomeActivityBinding>(activity, binding, savedInstance), IResult {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, data)

            when (response.type) {
                AuthenticationResponse.Type.CODE -> {
                    AppLog.INSTANCE.d("Callback spo", response.code)

                    //SpotifyAuthManager.getInstance().requestToken(SpotifyRequestToken())
                }

            // Auth flow returned an error
                AuthenticationResponse.Type.ERROR -> {
                    AppLog.INSTANCE.d("Callback spo", response.error)

                }
            }// Handle successful response
            // Handle error response
            // Most likely auth flow was cancelled
            // Handle other cases
        }
    }


    override fun onCreate(savedInstance: Bundle?) {
        activity.setSupportActionBar(binding.toolbar)

        val genericFragmentAdapter = object : GenericFragmentAdapter(activity) {
            override fun tabTitles(): List<FragmentTab>? {
                return arrayListOf(
                        FragmentTab(activity?.getString(R.string.drawer_filter_artiste), ArtistsFragment.newInstance()),
                        FragmentTab(activity?.getString(R.string.drawer_filter_album), AlbumsFragment.newInstance())
                )
            }
        }
        binding.viewPager.adapter = genericFragmentAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val rxPermission = RxPermissions(activity)
        if (!rxPermission.isGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE))
            rxPermission.request(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe({
                        granted ->
                        if (granted) {
                            binding.homeActivitySrl.isRefreshing = true
                            startSyncService()
                        }
                    })
        binding.homeActivitySrl.setOnRefreshListener {
            startSyncService()
        }

    }

    private fun startSyncService() {
        if (AppPrefs.getSpotifyAccessToken().isNullOrEmpty())
            AccountLinkFragment.newInstance().show(activity.supportFragmentManager,
                    AccountLinkFragment::class.java.name)
        LocalMusicManager.INSTANCE.observeSynchro()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    done ->
                    AppLog.INSTANCE.d("Synchro", "Done")
                    binding.homeActivitySrl.isRefreshing = false
                })
    }


}
