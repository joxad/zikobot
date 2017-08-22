package com.startogamu.zikobot

import android.app.Activity
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.view.View
import com.joxad.zikobot.data.db.model.ZikoArtist
import com.joxad.zikobot.data.db.model.ZikoPlaylist
import com.startogamu.zikobot.ftu.AccountLinkFragment
import com.startogamu.zikobot.home.artists.ArtistDetailActivity
import com.startogamu.zikobot.home.playlists.PlaylistDetailActivity
import com.startogamu.zikobot.player.alarm.AlarmBottomFragment


/**
 * Created by Jocelyn on 08/08/2017.
 */
object NavigationManager {
    fun goToPlaylist(context: Activity, model: ZikoPlaylist, v: View) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, v, context.getString(R.string.transition))
        context.startActivity(PlaylistDetailActivity.newInstance(context, model.id))//, options.toBundle())
    }

    fun goToArtist(context: Activity, model: ZikoArtist, v: View) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, v, context.getString(R.string.transition))
        context.startActivity(ArtistDetailActivity.newInstance(context, model.id))//, options.toBundle())
    }

    fun showAccount(activity: FragmentActivity) {
        AccountLinkFragment.newInstance().show(activity.supportFragmentManager,
                AccountLinkFragment::class.java.name)

    }

    fun showAlarmManagement(activity: FragmentActivity, id:Long) {
        AlarmBottomFragment.newInstance(id).show(activity.supportFragmentManager,
                AlarmBottomFragment::class.java.name)
    }


}