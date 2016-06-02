package com.startogamu.musicalarm.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.f2prateek.dart.HensonNavigable;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.startogamu.musicalarm.R;

/**
 * Created by josh on 02/06/16.
 */
@HensonNavigable
public class ActivityFirstStart extends AppIntro2 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name),getString(R.string.view_pager_1), R.drawable.view_pager_1, ContextCompat.getColor(this, R.color.colorPrimaryLight)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name),getString(R.string.view_pager_2), R.drawable.view_pager_2,ContextCompat.getColor(this, R.color.colorPrimaryLight)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name),getString(R.string.view_pager_3), R.mipmap.ic_launcher,ContextCompat.getColor(this, R.color.colorPrimaryLight)));
        // OPTIONAL METHODS
        // Override bar/separator color.

        // Hide Skip/Done button.

        setProgressButtonEnabled(true);

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
