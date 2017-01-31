package com.joxad.zikobot.app.search;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.data.event.LocalAlbumSelectEvent;
import com.joxad.zikobot.data.event.search.EventQueryChange;
import com.joxad.zikobot.app.spotify.FragmentSpotifySearch;
import com.lapism.searchview.SearchView;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.data.event.EventShowArtistDetail;
import com.joxad.zikobot.data.event.dialog.EventShowDialogSettings;
import com.joxad.zikobot.app.core.fragmentmanager.IntentManager;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.app.databinding.ActivitySearchBinding;
import com.joxad.zikobot.app.soundcloud.FragmentSoundCloudSearch;
import com.joxad.zikobot.app.home.ViewPagerAdapter;
import com.joxad.zikobot.app.alarm.DialogFragmentSettings;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by josh on 12/08/16.
 */
public class ActivitySearchVM extends ActivityBaseVM<ActivitySearch, ActivitySearchBinding> {
    private ViewPagerAdapter tabAdapter;

    /***
     * @param activity
     * @param binding
     */
    public ActivitySearchVM(ActivitySearch activity, ActivitySearchBinding binding) {
        super(activity, binding);
    }

    @Override
    public void onCreate() {
        initSearch();
        initTabLayout();
        uiHandler.postDelayed(() -> binding.searchView.showKeyboard(), 100);

    }

    private void initSearch() {
        binding.searchView.setVoiceText("Set permission on Android 6+ !");
        binding.searchView.setOnMenuClickListener(() -> activity.finish());
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                SearchManager.QUERY = newText;
                EventBus.getDefault().post(new EventQueryChange(newText));
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
    public void onEvent(EventShowDialogSettings event) {
        DialogFragmentSettings dialogFragmentSettings = DialogFragmentSettings.newInstance(event.getModel());
        dialogFragmentSettings.show(activity.getSupportFragmentManager(), DialogFragmentSettings.TAG);

    }


    @Subscribe
    public void onEvent(LocalAlbumSelectEvent localAlbumSelectEvent) {

        activity.startActivity(IntentManager.goToAlbum(localAlbumSelectEvent.getModel()));
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
        SearchManager.QUERY="";
    }

}
