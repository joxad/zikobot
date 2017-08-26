package com.startogamu.zikobot.player.alarm

import android.content.Context
import android.databinding.Bindable
import android.view.View
import com.joxad.easydatabinding.base.BaseVM
import com.joxad.zikobot.data.db.model.ZikoAlarm
import com.startogamu.zikobot.BR
import java.util.*

/**
 * Created by Jocelyn on 22/08/2017.
 */

class AlarmVM
/***
 *
 * @param context
 * @param model
 */
(context: Context, model: ZikoAlarm) : BaseVM<ZikoAlarm>(context, model) {


    override fun onCreate() {

    }

    fun updateTimeSelected(currentHour: Int, currentMin: Int) {
        model.minute = currentMin
        model.hour = currentHour
        notifyPropertyChanged(BR.alarmTime)
    }


    @Bindable
    fun getAlarmTime(): String {
        return String.format("%02d: %02d", model.hour, model.minute)
    }



    val isActivated: Boolean
        @Bindable
        get() = model.active


    fun activeDay(day: Int, aBoolean: Boolean?) {
        model.activeDay(day, aBoolean!!)
    }

    fun save() {
        AlarmManager.INSTANCE.prepareAlarm(context, model)
        AlarmManager.INSTANCE.refreshAlarm.onNext(model)
        notifyChange()
    }

    val minute: Int
        @Bindable
        get() = model.minute

    val hour: Int
        @Bindable
        get() = model.hour

    fun activeMonday(view: View) {
        activeDay(Calendar.MONDAY, !isMondayActive)
        notifyPropertyChanged(BR.mondayActive)
    }

    fun activeTuesday(view: View) {
        activeDay(Calendar.TUESDAY, !isTuesdayActive)
        notifyPropertyChanged(BR.tuesdayActive)
    }

    fun activeWed(view: View) {
        activeDay(Calendar.WEDNESDAY, !isWedActive)
        notifyPropertyChanged(BR.wedActive)
    }

    fun activeThursday(view: View) {
        activeDay(Calendar.THURSDAY, !isThursdayActive)
        notifyPropertyChanged(BR.thursdayActive)
    }

    fun activeFriday(view: View) {
        activeDay(Calendar.FRIDAY, !isFridayActive)
        notifyPropertyChanged(BR.fridayActive)
    }

    fun activeSaturday(view: View) {
        activeDay(Calendar.SATURDAY, !isSaturdayActive)
        notifyPropertyChanged(BR.saturdayActive)
    }

    fun activeSunday(view: View) {
        activeDay(Calendar.SUNDAY, !isSundayActive)
        notifyPropertyChanged(BR.sundayActive)
    }

    val isMondayActive: Boolean
        @Bindable
        get() = model.isDayActive(Calendar.MONDAY)

    val isTuesdayActive: Boolean
        @Bindable
        get() = model.isDayActive(Calendar.TUESDAY)

    val isWedActive: Boolean
        @Bindable
        get() = model.isDayActive(Calendar.WEDNESDAY)

    val isThursdayActive: Boolean
        @Bindable
        get() = model.isDayActive(Calendar.THURSDAY)

    val isFridayActive: Boolean
        @Bindable
        get() = model.isDayActive(Calendar.FRIDAY)

    val isSaturdayActive: Boolean
        @Bindable
        get() = model.isDayActive(Calendar.SATURDAY)

    val isSundayActive: Boolean
        @Bindable
        get() = model.isDayActive(Calendar.SUNDAY)


    val randomTrack: Int
        @Bindable
        get() = model.randomTrack

    val volume: Int
        @Bindable
        get() = model.volume

    fun updateRepeated(checked: Boolean) {
        model.repeated = if (checked) 1 else 0
        notifyChange()
    }


    fun updateRandom(checked: Boolean) {
        model.randomTrack = if (checked) 1 else 0
        notifyChange()
    }

    val isRepeated: Boolean
        @Bindable
        get() = model.repeated == 1

    fun updateVolume(progress: Int) {
        model.volume = progress
        notifyChange()
    }



}
