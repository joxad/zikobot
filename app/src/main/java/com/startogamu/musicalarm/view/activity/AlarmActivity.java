package com.startogamu.musicalarm.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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

    ActivityAlarmBinding binding;

    ActivityAlarmViewModel viewModel;

    @InjectExtra
    Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> finish());
        viewModel = new ActivityAlarmViewModel(this, binding);
        binding.setActivityAlarmViewModel(viewModel);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        viewModel.onDestroy();
        super.onDestroy();

    }

}
