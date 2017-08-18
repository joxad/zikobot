package com.startogamu.zikobot.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.joxad.easydatabinding.base.IVM
import com.joxad.zikobot.data.AppPrefs
import com.joxad.zikobot.data.db.CurrentPlaylistManager
import com.joxad.zikobot.data.db.model.ZikoTrack
import com.joxad.zikobot.data.player.PlayerService
import com.startogamu.zikobot.BR
import com.startogamu.zikobot.R
import com.startogamu.zikobot.databinding.PlayerViewBottomBinding
import com.startogamu.zikobot.home.track.TrackVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Jocelyn on 12/12/2016.
 */

class PlayerVM(private val activity: AppCompatActivity, private val binding: PlayerViewBottomBinding?) : BaseObservable(), IVM {

    val isBound = ObservableBoolean(false)
    val isExpanded = ObservableBoolean(false)
    var seekBarValue = ObservableField(0)
    var itemBinding: ItemBinding<TrackVM> = ItemBinding.of<TrackVM>(BR.trackVM, R.layout.track_item_player)
    lateinit var showList: ObservableBoolean
    private var musicConnection: ServiceConnection? = null
    private var playerService: PlayerService? = null
    private var intent: Intent? = null
    private var behavior: BottomSheetBehavior<View>? = null

    init {
        onCreate(null)
    }

    override fun onCreate(savedInstance: Bundle?) {

        showList = ObservableBoolean(false)
        intent = Intent(activity, PlayerService::class.java)
        if (binding == null)
            return
        behavior = BottomSheetBehavior.from<View>(binding.root.parent as CardView)
        behavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    isExpanded.set(true)
                else {
                    if (isExpanded.get())
                        isExpanded.set(false)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        CurrentPlaylistManager.INSTANCE.refreshSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { zikoTrack ->
                    notifyPropertyChanged(BR.currentTrackVM)
                    notifyPropertyChanged(BR.items)
                }
    }

    override fun onResume() {
        if (isBound.get())
            return
        musicConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                val binder = iBinder as PlayerService.PlayerBinder
                playerService = binder.service
                isBound.set(true)
                refresh()
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                isBound.set(false)
            }
        }
        activity.bindService(intent, musicConnection, Context.BIND_AUTO_CREATE)
        if (binding != null)
            rotateCD()
    }


    private fun refresh() {
        notifyChange()
    }


    /**
     * @param view
     */
    fun playPause(view: View) {
        if (isPlaying)
            playerService!!.pause()
        else
            playerService!!.resume()
        notifyPropertyChanged(BR.playing)
    }


    override fun onPause() {
        isBound.set(false)
        activity.unbindService(musicConnection)
    }

    override fun onDestroy() {}


    val isPlaying: Boolean
        @Bindable
        get() {
            if (!isBound.get())
                return false
            return playerService!!.playing
        }


    val position: Int
        @Bindable
        get() {
            if (!isBound.get())
                return 0
            return seekBarValue.get()
        }

    val positionMax: Int
        @Bindable
        get() {
            if (!isBound.get())
                return 0
            return playerService!!.positionMax()
        }

    fun onValueChanged(seekBar: SeekBar, progresValue: Int, fromUser: Boolean) {
        seekBarValue.set(progresValue)
        if (fromUser)
            playerService!!.seekTo(progresValue)
    }

    /**
     * Handle the back press

     * @return
     */
    fun onBackPressed(): Boolean {
        if (behavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            return false
        }
        return true
    }

    fun dismiss(view: View) {
        behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun expand(view: View) {
        behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    /**

     */
    private fun rotateCD() {
        binding!!.layoutVinyl?.rlPlayer?.animate()?.rotationBy(if (isPlaying) 1f else 0f)
                ?.setDuration(20)?.withEndAction { this.rotateCD() }
    }

    /**
     * Trick click to avoid to click items below the player

     * @param view
     */
    fun trickClick(view: View) {

    }

    fun next(view: View) {
        CurrentPlaylistManager.INSTANCE.next()
    }

    fun previous(view: View) {
        CurrentPlaylistManager.INSTANCE.previous()
    }

    /***
     * @param view
     */
    fun showListTracks(view: View) {
        showList.set(!showList.get())
    }

    fun clickVinyl(view: View) {
        if (!AppPrefs.bonusModeActivated()) {
            AppPrefs.bonusMode(AppPrefs.bonusMode() + 1)
        } else {
            if (AppPrefs.bonusMode() == 9)
                Toast.makeText(activity, "Bonus mode activated, have fun :)", Toast.LENGTH_SHORT).show()
        }
    }

    val currentTrackVM: TrackVM
        @Bindable
        get() {
            if (playerService != null)
                return TrackVM(activity, CurrentPlaylistManager.INSTANCE.currentTrack!!)
            else {
                return TrackVM(activity, ZikoTrack.empty())
            }
        }

    val items: List<TrackVM>
        @Bindable
        get() {
            val trackVMS = ArrayList<TrackVM>()
            for (zikoTrack in CurrentPlaylistManager.INSTANCE.getTracks()) {
                trackVMS.add(TrackVM(activity, zikoTrack).apply { playing.set(model.id == currentTrackVM.model.id) })
            }
            return trackVMS
        }

    /***
     * Return am or pm value

     * @param hour
     * *
     * @return
     */
    fun amPm(hour: Int): String {
        val am = "AM"
        val pm = "PM"
        var after = am
        if (hour >= 12) {
            after = pm
        }
        return after
    }

    fun amPmHour(hour: Int): Int {
        var hour = hour
        if (hour > 12)
            hour -= 12
        else {
            if (hour == 0)
                hour = 12
        }
        return hour
    }


    fun readableTime(millis: Long): String {
        return String.format("%2d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )
    }

}