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
        try {
            new SpotifyManager.Builder()
                    .setContext(this)
                    .setApiCallback(getString(R.string.api_spotify_callback))
                    .setApiKey(getString(R.string.api_spotify_id))
                    .setScope(new String[]{"user-read-private", "streaming"})
                    .setConnectionType(AuthenticationResponse.Type.CODE)
                    .build();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }

        /*SpotifyManager.loginWithBrowser(new SpotifyManager.OAuthListener() {
            @Override
            public void onReceived(String code) {
                Log.d(TAG, code);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, error);
            }
        });
*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SpotifyManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SpotifyManager.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotifyManager.destroy();
    }
}
