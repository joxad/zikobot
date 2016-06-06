package com.startogamu.zikobot.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.f2prateek.dart.HensonNavigable;
import com.github.paolorotolo.appintro.AppIntro;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.view.fragment.IntroFragment;

/**
 * Created by josh on 02/06/16.
 */
@HensonNavigable
public class ActivityFirstStart extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppPrefs.saveFirstStart(false);
        addSlide(IntroFragment.newInstance(R.layout.fragment_step_1));
        addSlide(IntroFragment.newInstance(R.layout.fragment_step_2));
        addSlide(IntroFragment.newInstance(R.layout.fragment_step_3));

        // OPTIONAL METHODS
        // Override bar/separator color.

        // Hide Skip/Done button.

        setZoomAnimation();
        setProgressButtonEnabled(true);
        setDoneText(getString(R.string.done));
        setSkipText(getString(R.string.skip));
    }


    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }
}
