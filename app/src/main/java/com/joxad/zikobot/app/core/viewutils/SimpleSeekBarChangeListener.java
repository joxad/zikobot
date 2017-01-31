package com.joxad.zikobot.app.core.viewutils;

import android.widget.SeekBar;

/**
 * Created by josh on 25/07/16.
 */
public abstract class SimpleSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
    @Override
    public abstract void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
