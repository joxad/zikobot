package com.startogamu.musicalarm.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivitySpotifyBinding;
import com.startogamu.musicalarm.viewmodel.ActivitySpotifyViewModel;

/**
 * Created by josh on 25/03/16.
 */
public class SpotifyActivity extends AppCompatActivity {


    ActivitySpotifyBinding binding;
    ActivitySpotifyViewModel activitySpotifyViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spotify);
        activitySpotifyViewModel = new ActivitySpotifyViewModel(this,binding);
        binding.setActivitySpotifyViewModel(activitySpotifyViewModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activitySpotifyViewModel.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        activitySpotifyViewModel.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activitySpotifyViewModel.onDestroy();
    }

}
