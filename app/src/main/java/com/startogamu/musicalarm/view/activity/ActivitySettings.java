package com.startogamu.musicalarm.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.f2prateek.dart.HensonNavigable;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivitySettingsBinding;
import com.startogamu.musicalarm.viewmodel.ActivitySettingsViewModel;

/**
 * Created by josh on 25/03/16.
 */
@HensonNavigable
public class ActivitySettings extends AppCompatActivity {


    ActivitySettingsBinding binding;
    ActivitySettingsViewModel activitySettingsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        setSupportActionBar(binding.toolbar);

        activitySettingsViewModel = new ActivitySettingsViewModel(this,binding);
        binding.setActivitySettingsViewModel(activitySettingsViewModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activitySettingsViewModel.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        activitySettingsViewModel.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activitySettingsViewModel.destroy();
    }

}
