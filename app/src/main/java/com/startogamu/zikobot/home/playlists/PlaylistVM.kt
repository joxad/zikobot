package com.startogamu.zikobot.home.playlists

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.databinding.Bindable
import android.os.Build
import android.provider.Settings
import android.support.v4.app.FragmentActivity
import android.view.View
import com.joxad.easydatabinding.base.BaseVM
import com.startogamu.zikobot.player.alarm.AlarmManager
import com.joxad.zikobot.data.db.model.ZikoPlaylist
import com.startogamu.zikobot.BR
import com.startogamu.zikobot.NavigationManager
import com.startogamu.zikobot.R
import com.startogamu.zikobot.player.alarm.AlarmVM

/**
 * Created by Jocelyn on 31/07/2017.
 */

open class PlaylistVM(section: Boolean, context: Context, model: ZikoPlaylist) : BaseVM<ZikoPlaylist>(context, model) {

    lateinit var notificationManager: NotificationManager
    override fun onCreate() {
    }

    @Bindable
    fun getName(): String? {
        return model.name
    }

    @Bindable
    fun getImage(): String? {
        return model.imageUrl
    }

    @Bindable
    fun getIcon(): Int? {
        if (model.spotifyId != null)
            return R.drawable.ic_spotify_green
        return null
    }

    @Bindable
    fun getNbTracks(): Int? {
        return model.nbTracks
    }

    open fun onClick(@SuppressWarnings("unused") v: View) {
        NavigationManager.goToPlaylist(context as Activity, model
                , v.findViewById(R.id.shared_element))
    }

    @Bindable
    fun getAlarm(): Boolean {
        val alarm = AlarmManager.INSTANCE.getAlarmByPlaylistId(model.id)
        return if (alarm != null) {
            alarm.active
        } else
            false
    }

    fun prepareAlarm(activity: FragmentActivity) {

        val zikoPlaylist = model
        if (zikoPlaylist != null) {
            val created = AlarmManager.INSTANCE.createAlarmOfPlaylist(context, zikoPlaylist)
            if (created) {
                notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !notificationManager.isNotificationPolicyAccessGranted) {
                    val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    context.startActivity(intent)
                }
                showEditAlarm(activity)
            }
        }

        notifyPropertyChanged(BR.alarm)
    }

    fun showEditAlarm(activity: FragmentActivity) {
        NavigationManager.showAlarmManagement(activity, model.id)
    }

    fun getAlarmTitle(): String? {
        if (getAlarm()) {

            val alarmVM = AlarmVM(context, AlarmManager.INSTANCE.getAlarmByPlaylistId(model.id))
            return context.getString(R.string.alarm_edit) +" "+ alarmVM.getAlarmTime() + " "
        }
        return null
    }
}
