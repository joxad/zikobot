package com.startogamu.zikobot.ftu

import android.app.Activity
import android.content.Context
import android.databinding.Bindable
import android.support.v4.content.ContextCompat
import android.view.View
import com.joxad.androidtemplate.core.log.AppLog
import com.joxad.zikobot.data.module.accounts.AccountManager
import com.joxad.zikobot.data.module.accounts.ZikoAccount
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyUser
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.startogamu.zikobot.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Jocelyn on 06/08/2017.
 */

class SpotifyAccountVM
/***

 * @param context
 * *
 * @param model
 */
(context: Context, model: SpotifyUser?) : AAccountVM(context, ZikoAccount.fromSpotify(model)) {
    override val userName: String?
        get() = model.userName
    override val userImage: String?
        get() = model.avatar

    override fun onCreate() {
        super.onCreate()
        init()

        AccountManager.INSTANCE.spotifyUserBehaviorSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    user: SpotifyUser? ->
                    model = ZikoAccount.fromSpotify(user)
                    loading.set(false)
                    init()
                    notifyChange()
                }, {
                    AppLog.INSTANCE.e("Spotify", it.localizedMessage)
                })
    }

    private fun init() {

        if (model.userName == null) {
            show.set(true)
        } else {
            show.set(false)
        }
    }

    override fun onClick(view: View) {
        loading.set(true)
        val builder = AuthenticationRequest.Builder(context.getString(R.string.api_spotify_id),
                AuthenticationResponse.Type.CODE,
                context.getString(R.string.zikobot_callback))
        builder.setShowDialog(true);
        builder.setScopes(arrayOf("streaming", "user-read-private"))
        val request = builder.build()

        AuthenticationClient.openLoginInBrowser(context as Activity,
                request)
    }

    override fun deleteClick(view: View) {
        AccountManager.INSTANCE.clearSpotify()
    }

    override val title: String
        get() = context.getString(R.string.spotify)


    override val icon: Int
        @Bindable
        get() = R.drawable.ic_spotify_green

    override val color: Int
        @Bindable
        get() = ContextCompat.getColor(context, R.color.colorSpotify)
}
