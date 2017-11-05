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
import com.startogamu.zikobot.NavigationManager
import com.startogamu.zikobot.R
import com.startogamu.zikobot.databinding.HomeActivityBinding
import com.startogamu.zikobot.home.albums.AlbumsFragment
import com.startogamu.zikobot.home.artists.ArtistsFragment
import com.startogamu.zikobot.home.playlists.PlaylistsFragment
import com.startogamu.zikobot.home.reco.RecoFragment
import com.startogamu.zikobot.player.PlayerVM
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Jocelyn on 27/07/2017.
 */

class HomeActivityVM(activity: HomeActivity?, binding: HomeActivityBinding?, savedInstance: Bundle?) :
        ActivityBaseVM<HomeActivity, HomeActivityBinding>(activity, binding, savedInstance), INewIntent {

    lateinit var playerVM: PlayerVM
    private var disposable: Disposable? = null
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
                        FragmentTab(activity?.getString(R.string.drawer_filter_recos), RecoFragment.newInstance()),
                        FragmentTab(activity?.getString(R.string.drawer_filter_playlist), PlaylistsFragment.newInstance()),
                        FragmentTab(activity?.getString(R.string.drawer_filter_artiste), ArtistsFragment.newInstance()),
                        FragmentTab(activity?.getString(R.string.drawer_filter_album), AlbumsFragment.newInstance())
                )
            }
        }
        binding.viewPager.adapter = genericFragmentAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)


        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_account -> showAccount()
            }
            true
        }
        playerVM = PlayerVM(activity, binding.viewPlayer)

    }


    override fun onResume() {
        super.onResume()
        playerVM.onResume()

    }

    override fun onBackPressed(): Boolean {

        return playerVM.onBackPressed()
    }



    override fun onPause() {
        super.onPause()
        playerVM.onPause()

    }

    private fun showAccount() {
        NavigationManager.showAccount(activity)
    }


}
