package com.startogamu.musicalarm.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityMusicBinding;
import com.startogamu.musicalarm.view.fragment.LocalMusicFragment;
import com.startogamu.musicalarm.view.fragment.SpotifyConnectFragment;
import com.startogamu.musicalarm.view.fragment.SpotifyMusicFragment;

/**
 * Created by josh on 26/03/16.
 */
public class ActivityMusicViewModel extends BaseObservable implements ViewModel {


    private final ActivityMusicBinding binding;
    private final AppCompatActivity context;

    /***
     * @param context
     * @param binding
     */
    public ActivityMusicViewModel(AppCompatActivity context, ActivityMusicBinding binding) {
        this.context = context;
        this.binding = binding;

        context.getSupportFragmentManager().beginTransaction().replace(R.id.container, LocalMusicFragment.newInstance()).commit();
        createBottomNavigation(binding.bottomNavigation);
    }


    public void createBottomNavigation(BottomNavigationView bottomNavigationView) {

        BottomNavigationItem local = new BottomNavigationItem
                (context.getString(R.string.activity_music_local), ContextCompat.getColor(context,R.color.colorPrimary), R.drawable.ic_folder);
        BottomNavigationItem spotify = new BottomNavigationItem
                (context.getString(R.string.activity_music_spotify), ContextCompat.getColor(context, android.R.color.holo_green_dark), R.drawable.ic_folder);
        BottomNavigationItem deezer = new BottomNavigationItem
                (context.getString(R.string.activity_music_deezer), ContextCompat.getColor(context, android.R.color.holo_orange_dark), R.drawable.ic_folder);

        bottomNavigationView.addTab(local);
        bottomNavigationView.addTab(spotify);
        bottomNavigationView.addTab(deezer);
        bottomNavigationView.setOnBottomNavigationItemClickListener(index -> {
            switch (index) {
                case 0:
                    context.getSupportFragmentManager().beginTransaction().replace(R.id.container, LocalMusicFragment.newInstance()).commit();
                    break;
                case 1:
                   // if (spotifyManager.hasAccessToken()) {
                     if( false) {
                        context.getSupportFragmentManager().beginTransaction().replace(R.id.container, SpotifyMusicFragment.newInstance()).commit();
                    } else {
                        context.getSupportFragmentManager().beginTransaction().replace(R.id.container, SpotifyConnectFragment.newInstance()).commit();
                    }
                    break;
                case 2:
                    break;
            }
        });
    }

    @Override
    public void onDestroy() {

    }


}
