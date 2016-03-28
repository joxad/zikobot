package com.startogamu.musicalarm.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityAlarmBinding;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.viewmodel.ActivityAlarmViewModel;

/**
 * Created by josh on 08/03/16.
 */
public class AlarmActivity extends AppCompatActivity {

    private static final String EXTRA_ALARM = "ALARM";
    ActivityAlarmBinding binding;

    ActivityAlarmViewModel viewModel;

    @InjectExtra
    Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dart.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> finish());

        if (alarm != null) {
            viewModel = new ActivityAlarmViewModel(this, binding, alarm);
            setTitle(alarm.getName());
        } else {
            viewModel = new ActivityAlarmViewModel(this, binding);
            setTitle(getString(R.string.activity_alarm_new));
        }
        binding.setActivityAlarmViewModel(viewModel);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        viewModel.onDestroy();
        super.onDestroy();

    }

}
