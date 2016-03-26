package com.startogamu.musicalarm.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.f2prateek.dart.HensonNavigable;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityMusicBinding;
import com.startogamu.musicalarm.viewmodel.ActivityMusicViewModel;

/**
 * Created by josh on 26/03/16.
 */
@HensonNavigable
public class MusicActivity extends AppCompatActivity {

    ActivityMusicBinding binding;
    ActivityMusicViewModel activityMusicViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> finish());
        activityMusicViewModel = new ActivityMusicViewModel(this, binding);
        binding.setActivityMusicViewModel(activityMusicViewModel);

    }

    @Override
    protected void onPause() {
        super.onPause();
        activityMusicViewModel.onDestroy();
    }
}
