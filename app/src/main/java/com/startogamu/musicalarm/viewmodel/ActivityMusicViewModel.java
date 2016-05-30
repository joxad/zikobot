package com.startogamu.musicalarm.viewmodel;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.support.v4.content.ContextCompat;

import com.joxad.easydatabinding.base.IVM;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.AppPrefs;
import com.startogamu.musicalarm.core.utils.REQUEST;
import com.startogamu.musicalarm.databinding.ActivityMusicBinding;
import com.startogamu.musicalarm.view.activity.BaseActivity;
import com.startogamu.musicalarm.view.fragment.DeezerFragment;
import com.startogamu.musicalarm.view.fragment.LocalMusicFragment;
import com.startogamu.musicalarm.view.fragment.SpotifyConnectFragment;
import com.startogamu.musicalarm.view.fragment.SpotifyMusicFragment;

/***
 * {@link ActivityMusicViewModel} handle multiples fragments :
 * <ul>
 * <li>{@link SpotifyMusicFragment } taht show the differents playlist to the user. Can redirect to
 * {@link com.startogamu.musicalarm.view.fragment.SpotifyPlaylistTracksFragment}</li>
 * <li>{@link SpotifyConnectFragment} that handle the connection to spotify</li>
 * <li>{@link LocalMusicFragment}</li>
 * </ul>
 */
public class ActivityMusicViewModel extends BaseObservable implements IVM {


    private final ActivityMusicBinding binding;
    private final BaseActivity context;

    private SpotifyMusicFragment spotifyMusicFragment;
    private SpotifyConnectFragment spotifyConnectFragment;
    private LocalMusicFragment localMusicFragment;

    /***
     * @param context
     * @param binding
     */
    public ActivityMusicViewModel(BaseActivity context, ActivityMusicBinding binding) {
        this.context = context;
        this.binding = binding;
        localMusicFragment = LocalMusicFragment.newInstance();
        context.replaceFragment(localMusicFragment, false);
        createBottomNavigation(binding.bottomNavigation);
    }


    /***
     * @param bottomNavigationView
     */
    public void createBottomNavigation(BottomNavigationView bottomNavigationView) {

        BottomNavigationItem local = new BottomNavigationItem
                (context.getString(R.string.activity_music_local), ContextCompat.getColor(context, R.color.colorPrimary), R.drawable.ic_folder);
        BottomNavigationItem spotify = new BottomNavigationItem
                (context.getString(R.string.activity_music_spotify), ContextCompat.getColor(context, android.R.color.holo_green_dark), R.drawable.logo_spotify);
        BottomNavigationItem deezer = new BottomNavigationItem
                (context.getString(R.string.activity_music_deezer), ContextCompat.getColor(context, android.R.color.holo_orange_dark), R.drawable.logo_deezer);

        bottomNavigationView.addTab(local);
        bottomNavigationView.addTab(spotify);
        bottomNavigationView.addTab(deezer);
        bottomNavigationView.setOnBottomNavigationItemClickListener(index -> {
            switch (index) {
                case 0:
                    context.replaceFragment(localMusicFragment, false);
                    break;
                case 1:
                    // if (spotifyManager.hasAccessToken()) {
                    if (!Prefs.contains(AppPrefs.SPOTIFY_ACCESS_CODE)) {
                        spotifyConnectFragment = SpotifyConnectFragment.newInstance();
                        context.replaceFragment(spotifyConnectFragment, false);
                    } else {
                        loadSpotifyMusicFragment();
                    }
                    break;
                case 2:
                    context.replaceFragment(DeezerFragment.newInstance(), false);
                    break;
            }
        });
    }

    /***
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (spotifyConnectFragment != null)
            spotifyConnectFragment.onActivityResult(requestCode, resultCode, data);
    }

    /***
     * @param intent
     */
    public void onNewIntent(Intent intent) {
        if (spotifyConnectFragment != null && !spotifyConnectFragment.isDetached())
            spotifyConnectFragment.onNewIntent(intent);
    }

    /***
     *
     */
    public void loadSpotifyMusicFragment() {
        spotifyMusicFragment = SpotifyMusicFragment.newInstance();
        context.replaceFragment(spotifyMusicFragment, false);
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (localMusicFragment == null || localMusicFragment.isDetached())
            return;
        switch (requestCode) {
            case REQUEST.PERMISSION_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    localMusicFragment.loadMusic();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
