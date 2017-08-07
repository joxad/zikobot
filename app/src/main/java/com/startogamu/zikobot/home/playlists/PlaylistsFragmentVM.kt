package com.startogamu.zikobot.home.playlists

import android.databinding.ObservableArrayList
import android.os.Bundle
import com.joxad.androidtemplate.core.log.AppLog
import com.joxad.easydatabinding.fragment.v4.FragmentRecyclerBaseVM
import com.joxad.zikobot.data.AppPrefs
import com.joxad.zikobot.data.db.model.ZikoPlaylist
import com.joxad.zikobot.data.module.accounts.AccountManager
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager
import com.startogamu.zikobot.BR
import com.startogamu.zikobot.R
import com.startogamu.zikobot.databinding.PlaylistsFragmentBinding

/**
 * Generated by generator-android-template
 */
class PlaylistsFragmentVM(fragment: PlaylistsFragment, binding: PlaylistsFragmentBinding, savedInstance: Bundle?)
    : FragmentRecyclerBaseVM<PlaylistVM, PlaylistsFragment, PlaylistsFragmentBinding>(fragment, binding, savedInstance) {
    override fun itemData(): Int {
        return BR.playlistVM
    }

    override fun itemLayoutResource(): Int {
        return R.layout.playlist_item
    }

    override fun onCreate(savedInstance: Bundle?) {
        items = ObservableArrayList()
        syncSpotify()
        AccountManager.INSTANCE.spotifyUserBehaviorSubject.subscribe({
            syncSpotify()
        }, {
            AppLog.INSTANCE.e("Spotify", it.localizedMessage)
        })

    }


    private fun syncSpotify() {
        if (!AppPrefs.getRefreshToken().isNullOrEmpty()) {
            SpotifyApiManager.INSTANCE.userPlaylists.
                    subscribe({
                        for (spo in it.items) {
                            val zikoP = ZikoPlaylist.fromSpotifyPlaylist(spo)
                            zikoP.save()
                            items.add(PlaylistVM(false, fragment.context, zikoP))

                        }
                    }, {
                        AppLog.INSTANCE.e("Spotify", it.localizedMessage)
                    })
        }
    }

    companion object {

        private val TAG = PlaylistsFragmentVM::class.java.simpleName
    }
}
