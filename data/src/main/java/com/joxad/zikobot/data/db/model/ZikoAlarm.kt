package com.joxad.zikobot.data.db.model


import com.joxad.zikobot.data.db.ZikoDB
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

/**
 * Created by josh on 08/03/16.
 */
@Table(database = ZikoDB::class)
class ZikoAlarm : BaseModel() {

    @ForeignKey(stubbedRelationship = true)
    var zikoPlaylist: ZikoPlaylist? = null

    @PrimaryKey(autoincrement = true)
    var id: Int = 0

    @Column
    var hour: Int = 0
    @Column
    var minute: Int = 0
    @Column(getterName = "getActive")
    var active: Boolean
    @Column
    var repeated: Int = 0
    @Column
    var days = "-0000000"
    @Column
    var randomTrack: Int = 0
    @Column
    var volume: Int = 0
    @Column
    var timeInMillis: Long = 0

    /***
     * Default constructor =>  8 00 am
     */
    init {
        hour = 8
        minute = 0
        active = false
        volume = -1
    }


    val isRandom: Boolean
        get() = randomTrack == 1

    fun isDayActive(day: Int): Boolean {
        val daysBuilder = StringBuilder(days)
        val dayStatus = daysBuilder[day]
        return dayStatus == '1'
    }

    fun activeDay(day: Int, aBoolean: Boolean) {
        replaceChar(if (aBoolean) '1' else '0', day)
    }

    fun replaceChar(c: Char, index: Int) {
        val daysBuilder = StringBuilder(days)
        daysBuilder.setCharAt(index, c)
        days = daysBuilder.toString()
    }

}
