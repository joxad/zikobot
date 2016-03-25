package com.startogamu.musicalarm.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityAlarmsBinding;
import com.startogamu.musicalarm.viewmodel.ActivityAlarmsViewModel;

public class AlarmsActivity extends AppCompatActivity {

    ActivityAlarmsBinding binding;
    private String TAG = AlarmsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarms);
        setSupportActionBar(binding.toolbar);

        ActivityAlarmsViewModel mainViewModel = new ActivityAlarmsViewModel(this);
        binding.setActivityAlarmsViewModel(mainViewModel);
        binding.alarmRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
