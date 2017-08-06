package com.startogamu.zikobot.ftu

import android.os.Bundle
import android.view.View
import com.joxad.easydatabinding.bottomsheet.DialogBottomSheetBaseVM
import com.joxad.zikobot.data.AppPrefs
import com.joxad.zikobot.data.module.accounts.AccountManager
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyUser
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.startogamu.zikobot.R
import com.startogamu.zikobot.databinding.AccountLinkFragmentBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Generated by generator-android-template
 */
class AccountLinkFragmentVM
/***
 * @param fragment
 * *
 * @param binding
 */
(fragment: AccountLinkFragment, binding: AccountLinkFragmentBinding) :
        DialogBottomSheetBaseVM<AccountLinkFragment, AccountLinkFragmentBinding>(fragment, binding) {

    lateinit var spotifyAAccountVM: AAccountVM

    override fun onCreate() {
        spotifyAAccountVM = SpotifyAccountVM(fragment.activity, AppPrefs.spotifyUser())
    }

    override fun onCreate(savedInstance: Bundle?) {

    }

    companion object {
        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.

        private val TAG = AccountLinkFragmentVM::class.java.simpleName
    }
}
