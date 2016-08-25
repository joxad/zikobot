package com.startogamu.zikobot.search;

import android.os.Handler;
import android.os.Looper;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.lapism.searchview.SearchView;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventShowArtistDetail;
import com.startogamu.zikobot.core.event.LocalAlbumSelectEvent;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.databinding.ActivitySearchBinding;
import com.startogamu.zikobot.view.adapter.ViewPagerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by josh on 12/08/16.
 */
public class ActivitySearchVM extends ActivityBaseVM<ActivitySearch, ActivitySearchBinding> {
    Handler handler;
    private ViewPagerAdapter tabAdapter;

    /***
     * @param activity
     * @param binding
     */
    public ActivitySearchVM(ActivitySearch activity, ActivitySearchBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        handler = new Handler(Looper.getMainLooper());
        initSearch();
        initTabLayout();
        handler.postDelayed(() -> binding.searchView.showKeyboard(), 100);

    }

    private void initSearch() {
        binding.searchView.setVoiceText("Set permission on Android 6+ !");
        binding.searchView.setOnMenuClickListener(() -> activity.finish());
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.searchView.hideKeyboard();
                return true;
            }
        });
        binding.searchView.open(true);
    }


    private void initTabLayout() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        tabAdapter = new ViewPagerAdapter(activity, activity.getSupportFragmentManager());
        binding.viewPager.setAdapter(tabAdapter);
        // Give the TabLayout the ViewPager
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        tabAdapter.addFragment(activity.getString(R.string.activity_music_local), FragmentSearch.newInstance());

    }


    public void onResume() {
        EventBus.getDefault().register(this);
        if (AppPrefs.spotifyUser() != null)
            tabAdapter.addFragment(activity.getString(R.string.activity_music_spotify), FragmentSpotifySearch.newInstance());
        if (AppPrefs.soundCloudUser() != null) {
            tabAdapter.addFragment(activity.getString(R.string.soundcloud), FragmentSoundCloudSearch.newInstance());
        }
        if (AppPrefs.deezerUser() != null) {
            tabAdapter.addFragment(activity.getString(R.string.activity_music_deezer), FragmentDeezerSearch.newInstance());
        }
        tabAdapter.notifyDataSetChanged();

    }

    @Subscribe
    public void onEvent(EventShowArtistDetail eventShowArtistDetail) {
        activity.startActivity(IntentManager.goToArtist(eventShowArtistDetail.getArtist()));
    }

    @Subscribe
    public void onEvent(LocalAlbumSelectEvent localAlbumSelectEvent) {

        activity.startActivity(IntentManager.goToAlbum(localAlbumSelectEvent.getModel()));
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void destroy() {

    }
}
