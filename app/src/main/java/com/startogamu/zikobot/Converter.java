package com.startogamu.zikobot;

import android.support.v7.widget.RecyclerView;
import android.widget.TimePicker;

import com.joxad.androidtemplate.core.view.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

/**
 * Created by Jocelyn on 21/08/2017.
 */

public class Converter {

    public void init() {
        TimePicker timePicker = new TimePicker(null);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {

            }
        });
    }
}
