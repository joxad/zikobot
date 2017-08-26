package com.startogamu.zikobot.player.alarm

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.joxad.easydatabinding.bottomsheet.DialogBottomSheetBaseVM
import com.philliphsu.bottomsheetpickers.time.grid.GridTimePickerDialog
import com.philliphsu.bottomsheetpickers.time.numberpad.NumberPadTimePickerDialog
import com.startogamu.zikobot.Constants
import com.startogamu.zikobot.databinding.AlarmFragmentBinding

/**
 * Created by Jocelyn on 22/08/2017.
 */

class AlarmBottomFragmentVM(fragment: AlarmBottomFragment, binding: AlarmFragmentBinding)
    : DialogBottomSheetBaseVM<AlarmBottomFragment, AlarmFragmentBinding>(fragment, binding) {

    lateinit var alarmVM: AlarmVM
    private var am: AudioManager? = null

    /***
     *
     */
    override fun onCreate() {
        val alarm = AlarmManager.INSTANCE.getAlarmByPlaylistId(fragment.arguments.getLong(Constants.Extra.PLAYLIST_ID))
        alarmVM = AlarmVM(fragment.context, alarm)
        am = fragment.activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        init()
    }


    /***
     *
     */
    fun save(@SuppressWarnings("unused") v: View) {
        alarmVM.save()
        fragment.dismiss()
    }

    /****
     *
     */
    private fun init() {
        val max = am!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        binding.seekBarVolume.max = am!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        binding.seekBarVolume.progress = if (alarmVM.volume == -1) (max * 0.5f).toInt() else alarmVM.volume
        binding.swRandom.isChecked = alarmVM.randomTrack == 1

        binding.swRandom.setOnCheckedChangeListener { _, b -> alarmVM.updateRandom(b) }

        binding.seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b)
                    alarmVM.updateVolume(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    public fun editTime(v: View) {
        /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
             binding.timePicker.minute = alarmVM.minute
             binding.timePicker.hour = alarmVM.hour
         } else {
             binding.timePicker.currentHour = alarmVM.hour
             binding.timePicker.currentMinute = alarmVM.minute
         }
         binding.timePicker.setOnTimeChangedListener { _, i, i1 ->
             alarmVM.updateTimeSelected(currentHour = i, currentMin = i1)
         }*/
        // Configured according to the system preference for 24-hour time.
        val pad = NumberPadTimePickerDialog.Builder({ _, i, i1 ->
            alarmVM.updateTimeSelected(currentHour = i, currentMin = i1)

        },true).setThemeDark(true).build()
        pad.show(fragment.activity.supportFragmentManager, pad.tag)

    }

    @Deprecated("")
    override fun onCreate(savedInstance: Bundle?) {

    }
}
