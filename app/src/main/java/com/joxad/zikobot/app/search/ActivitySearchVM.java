package com.joxad.zikobot.app.search;

import android.databinding.ObservableBoolean;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.alarm.DialogFragmentSettings;
import com.joxad.zikobot.app.core.fragmentmanager.IntentManager;
import com.joxad.zikobot.app.core.utils.ZikoUtils;
import com.joxad.zikobot.app.databinding.ActivitySearchBinding;
import com.joxad.zikobot.app.home.ViewPagerAdapter;
import com.joxad.zikobot.app.soundcloud.FragmentSoundCloudSearch;
import com.joxad.zikobot.app.spotify.FragmentSpotifySearch;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.event.EventShowArtistDetail;
import com.joxad.zikobot.data.event.LocalAlbumSelectEvent;
import com.joxad.zikobot.data.event.dialog.EventShowDialogSettings;
import com.joxad.zikobot.data.event.search.EventQueryChange;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

/**
 * Created by josh on 12/08/16.
 */
public class ActivitySearchVM extends ActivityBaseVM<ActivitySearch, ActivitySearchBinding> {
    private ViewPagerAdapter tabAdapter;
    public ObservableBoolean isSearchValid;

    /***
     * @param activity
     * @param binding
     */
    public ActivitySearchVM(ActivitySearch activity, ActivitySearchBinding binding) {
        super(activity, binding);
    }

    @Override
    public void onCreate() {
        isSearchValid = new ObservableBoolean(false);
        initSearch();
        initTabLayout();
        binding.etSearch.requestFocus();
        ZikoUtils.showKeyboard(binding.etSearch);

    }

    /**
     * Init the search using the event on the edittext
     */
    private void initSearch() {
        RxTextView
                .afterTextChangeEvents(binding.etSearch)
                .debounce(600, TimeUnit.MILLISECONDS)       // wait 300ms before executing
                .map(event -> event.editable().toString())
                .subscribe(this::search, throwable -> {
                    Logger.e(throwable.getLocalizedMessage());
                });
    }

    /***
     * @param s
     */
    private void search(String s) {
        EventBus.getDefault().post(new EventQueryChange(s));
        isSearchValid.set(s.length() > 2);
        SearchManager.QUERY = s;
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
    }

    @Override
    protected boolean onBackPressed() {
        SearchManager.QUERY = "";
        ZikoUtils.hideKeyboard(binding.etSearch);
        return super.onBackPressed();
    }
}
