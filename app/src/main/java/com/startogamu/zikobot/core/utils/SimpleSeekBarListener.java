package com.startogamu.zikobot.core.utils;

import android.widget.SeekBar;

/**
 * Created by josh on 12/05/16.
 */
public abstract class SimpleSeekBarListener implements SeekBar.OnSeekBarChangeListener {
    @Override
    public abstract void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
