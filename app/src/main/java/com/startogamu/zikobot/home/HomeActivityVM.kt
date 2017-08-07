package com.startogamu.zikobot.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.joxad.androidtemplate.core.adapter.GenericFragmentAdapter
import com.joxad.androidtemplate.core.fragment.FragmentTab
import com.joxad.androidtemplate.core.log.AppLog
import com.joxad.easydatabinding.activity.ActivityBaseVM
import com.joxad.easydatabinding.activity.INewIntent
import com.joxad.zikobot.data.AppPrefs
import com.joxad.zikobot.data.module.accounts.AccountManager
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.startogamu.zikobot.R
import com.startogamu.zikobot.databinding.HomeActivityBinding
import com.startogamu.zikobot.ftu.AccountLinkFragment
import com.startogamu.zikobot.home.albums.AlbumsFragment
import com.startogamu.zikobot.home.artists.ArtistsFragment
import com.startogamu.zikobot.home.playlists.PlaylistsFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by Jocelyn on 27/07/2017.
 */

class HomeActivityVM(activity: HomeActivity?, binding: HomeActivityBinding?, savedInstance: Bundle?) :
        ActivityBaseVM<HomeActivity, HomeActivityBinding>(activity, binding, savedInstance), INewIntent {
    override fun onNewIntent(intent: Intent?) {
        val uri = intent?.data
        if (uri != null) {
            val response = AuthenticationResponse.fromUri(uri)
            handleResponse(response)
        }
    }

    private fun handleResponse(response: AuthenticationResponse?) {
        when (response?.type) {
            AuthenticationResponse.Type.CODE -> {
                AppLog.INSTANCE.d("Callback spo", response.code)
                AppPrefs.saveAccessCode(response.code)
                SpotifyAuthManager.INSTANCE.requestToken(activity).subscribe({
                    SpotifyApiManager.INSTANCE.me.subscribe({
                        Toast.makeText(activity, "Welcome ${it.displayName}", Toast.LENGTH_SHORT).show()
                        AccountManager.INSTANCE.onSpotifyLogged(it)
                    }, {
                        Toast.makeText(activity, "Error ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                    })
                }, {
                    AppLog.INSTANCE.e("Spotify", it.localizedMessage)
                })
            }
            AuthenticationResponse.Type.ERROR -> {
                AppLog.INSTANCE.d("Callback spo", response.error)
            }
        }
    }


    override fun onCreate(savedInstance: Bundle?) {
        activity.setSupportActionBar(binding.toolbar)

        val genericFragmentAdapter = object : GenericFragmentAdapter(activity) {
            override fun tabTitles(): List<FragmentTab>? {
                return arrayListOf(
                        FragmentTab(activity?.getString(R.string.drawer_filter_playlist), PlaylistsFragment.newInstance()),
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
                            startSyncService()
                        }
                    })

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_account -> showAccount()
            }
            true
        }

    }

    private fun startSyncService() {
        if (AppPrefs.getSpotifyAccessToken().isNullOrEmpty())
            showAccount()
        LocalMusicManager.INSTANCE.observeSynchro()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    done ->
                    AppLog.INSTANCE.d("Synchro", "Done")
                })
    }

    private fun showAccount() {
        AccountLinkFragment.newInstance().show(activity.supportFragmentManager,
                AccountLinkFragment::class.java.name)
    }


}
