package com.joxad.zikobot.app.intro;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.data.AppPrefs;

/**
 * Created by josh on 02/06/16.
 */
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

        String[] permissions = new String[1];
        permissions[0] =  Manifest.permission.READ_EXTERNAL_STORAGE;
        askForPermissions(permissions,2);
        setZoomAnimation();
        setProgressButtonEnabled(true);
        setDoneText(getString(R.string.done));
        showSkipButton(false);
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
