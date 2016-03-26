package com.startogamu.musicalarm.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
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
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    AlarmsActivity.this.goToSettingsActivity();
                    return true;
                case R.id.action_about:
                    new LibsBuilder()
                            .withAboutAppName(getString(R.string.about))
                            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                            .start(AlarmsActivity.this);
                    return true;
                default:
                    return true;
            }
        });
        ActivityAlarmsViewModel mainViewModel = new ActivityAlarmsViewModel(this);
        binding.setActivityAlarmsViewModel(mainViewModel);
        binding.alarmRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarms, menu);
        return true;
    }

    public void goToSettingsActivity() {
        startActivity(Henson.with(this).gotoSettingsActivity().build());
    }

}
