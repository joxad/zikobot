package com.startogamu.zikobot.player

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.joxad.androidtemplate.core.log.AppLog
import com.joxad.easydatabinding.base.IVM
import com.joxad.zikobot.data.AppPrefs
import com.joxad.zikobot.data.db.CurrentPlaylistManager
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

    val isExpanded = ObservableBoolean(false)
    var seekBarValue = ObservableField(0)
    var itemBinding: ItemBinding<TrackVM> = ItemBinding.of<TrackVM>(BR.trackVM, R.layout.track_item_player)
    lateinit var showList: ObservableBoolean
    private var behavior: BottomSheetBehavior<View>? = null

    init {
        onCreate(null)
    }

    override fun onCreate(savedInstance: Bundle?) {

        showList = ObservableBoolean(false)
        if (binding == null)
            return
        behavior = BottomSheetBehavior.from<View>(binding.root.parent as CardView)
        behavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    isExpanded.set(true)
                } else {
                    if (isExpanded.get())
                        isExpanded.set(false)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        watchProgress()

        CurrentPlaylistManager.INSTANCE.resumeObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ ziko ->
                    notifyPropertyChanged(BR.playing)
                }) { err ->

                }

        CurrentPlaylistManager.INSTANCE.pauseObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ ziko ->
                    notifyPropertyChanged(BR.playing)
                }) { err ->

                }

        CurrentPlaylistManager.INSTANCE.refreshObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    notifyChange()
                }, { AppLog.INSTANCE.e("PlayerVM", it.localizedMessage) })
    }

    private fun watchProgress() {
        CurrentPlaylistManager.INSTANCE.currentPositionObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    seekBarValue.set(it)
                }, {

                })

    }

    override fun onResume() {
        refresh()

        if (binding != null)
            rotateCD()
    }

    override fun onPause() {

    }

    private fun refresh() {
        notifyChange()
    }


    /**
     * @param view
     */
    fun playPause(@SuppressWarnings("unused") view: View) {
        if (isPlaying)
            CurrentPlaylistManager.INSTANCE.pause(currentTrackVM.model)
        else
            if (seekBarValue.get() == 0)
                CurrentPlaylistManager.INSTANCE.play(currentTrackVM.model)
            else
                CurrentPlaylistManager.INSTANCE.resume(currentTrackVM.model)
        notifyPropertyChanged(BR.playing)
    }


    override fun onDestroy() {}


    val isPlaying: Boolean
        @Bindable
        get() {
            return CurrentPlaylistManager.INSTANCE.playing
        }


    val positionMax: Int
        @Bindable
        get() {
            return CurrentPlaylistManager.INSTANCE.positionMax()!!
        }

    fun onValueChanged(@SuppressWarnings("unused") seekBar: SeekBar, progresValue: Int, fromUser: Boolean) {
        seekBarValue.set(progresValue)
        if (fromUser)
            CurrentPlaylistManager.INSTANCE.seekTo(progresValue)
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

    fun dismiss(@SuppressWarnings("unused") view: View) {
        behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun expand(@SuppressWarnings("unused") view: View) {
        behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    /**

     */
    private fun rotateCD() {
        binding!!.layoutVinyl?.rlPlayer?.animate()?.rotationBy(if (CurrentPlaylistManager.INSTANCE.playing) 1f else 0f)
                ?.setDuration(20)?.withEndAction { this.rotateCD() }
    }

    /**
     * Trick click to avoid to click items below the player

     * @param view
     */
    fun trickClick(@SuppressWarnings("unused") view: View) {

    }

    fun next(@SuppressWarnings("unused") view: View) {
        CurrentPlaylistManager.INSTANCE.next()
    }

    fun previous(@SuppressWarnings("unused") view: View) {
        CurrentPlaylistManager.INSTANCE.previous()
    }

    /***
     * @param view
     */
    fun showListTracks(@SuppressWarnings("unused") view: View) {
        showList.set(!showList.get())
    }

    fun clickVinyl(@SuppressWarnings("unused") view: View) {
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
            return TrackVM(activity, CurrentPlaylistManager.INSTANCE.currentTrack!!)

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


    fun readableTime(millis: Long): String {
        return String.format("%2d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )
    }

}