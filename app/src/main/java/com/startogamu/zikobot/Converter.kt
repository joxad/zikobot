package com.startogamu.zikobot

import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker

import com.joxad.androidtemplate.core.view.utils.EndlessRecyclerOnScrollListener

import java.util.ArrayList

/**
 * Created by Jocelyn on 21/08/2017.
 */

class Converter {

    fun init() {
        val timePicker = TimePicker(null)
        timePicker.setOnTimeChangedListener { timePicker, i, i1 -> }
        val et = EditText(null)
        et.setOnEditorActionListener { textView, i, keyEvent -> false }
    }
}
