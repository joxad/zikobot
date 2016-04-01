package com.startogamu.musicalarm.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v4.content.ContextCompat;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.pixplicity.easyprefs.library.Prefs;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityMusicBinding;
import com.startogamu.musicalarm.utils.SpotifyPrefs;
import com.startogamu.musicalarm.view.activity.BaseActivity;
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
public class ActivityMusicViewModel extends BaseObservable implements ViewModel {


    private final ActivityMusicBinding binding;
    private final BaseActivity context;

    private SpotifyMusicFragment spotifyMusicFragment;
    private SpotifyConnectFragment spotifyConnectFragment;

    /***
     * @param context
     * @param binding
     */
    public ActivityMusicViewModel(BaseActivity context, ActivityMusicBinding binding) {
        this.context = context;
        this.binding = binding;
        try {
            new SpotifyManager.Builder()
                    .setContext(context)
                    .setApiKey(context.getString(R.string.api_spotify_id))
                    .setApiCallback(context.getString(R.string.api_spotify_callback_musics))
                    .setConnectionType(AuthenticationResponse.Type.CODE)
                    .setScope(new String[]{"user-read-private", "streaming"})
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.replaceFragment(LocalMusicFragment.newInstance(),false);
        createBottomNavigation(binding.bottomNavigation);
    }


    /***
     * @param bottomNavigationView
     */
    public void createBottomNavigation(BottomNavigationView bottomNavigationView) {

        BottomNavigationItem local = new BottomNavigationItem
                (context.getString(R.string.activity_music_local), ContextCompat.getColor(context, R.color.colorPrimary), R.drawable.ic_folder);
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
                    context.replaceFragment(LocalMusicFragment.newInstance(),false);
                    break;
                case 1:
                    // if (spotifyManager.hasAccessToken()) {
                    if (!Prefs.contains(SpotifyPrefs.ACCESS_CODE)) {
                        spotifyConnectFragment = SpotifyConnectFragment.newInstance();
                        context.replaceFragment(spotifyConnectFragment,false);
                    } else {
                        loadSpotifyMusicFragment();
                    }
                    break;
                case 2:
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
        if (spotifyConnectFragment != null)
            spotifyConnectFragment.onNewIntent(intent);
    }

    /***
     *
     */
    @Override
    public void onDestroy() {
    }

    /***
     *
     */
    public void loadSpotifyMusicFragment() {
        spotifyMusicFragment = SpotifyMusicFragment.newInstance();
        context.replaceFragment(spotifyMusicFragment,false);
    }
}
