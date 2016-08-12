package com.startogamu.zikobot.search;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityOptionsCompat;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.lapism.searchview.SearchView;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventShowArtistDetail;
import com.startogamu.zikobot.core.event.LocalAlbumSelectEvent;
import com.startogamu.zikobot.core.fragmentmanager.FragmentManager;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.databinding.ActivitySearchBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by josh on 12/08/16.
 */
public class ActivitySearchVM extends ActivityBaseVM<ActivitySearch, ActivitySearchBinding> {
    Handler handler;
    FragmentSearch fragmentSearch;

    /***
     * @param activity
     * @param binding
     */
    public ActivitySearchVM(ActivitySearch activity, ActivitySearchBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        handler= new Handler(Looper.getMainLooper());
        fragmentSearch = FragmentSearch.newInstance();
        FragmentManager.replaceFragment(activity, fragmentSearch, true, false);
        binding.searchView.setVoiceText("Set permission on Android 6+ !");
        binding.searchView.setOnMenuClickListener(() -> activity.finish());
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                fragmentSearch.query(newText);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.searchView.hideKeyboard();
                return true;
            }
        });
        binding.searchView.open(true);
        handler.postDelayed(() -> binding.searchView.showKeyboard(), 100);

    }


    public void onResume() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(EventShowArtistDetail eventShowArtistDetail) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, eventShowArtistDetail.getView(), activity.getString(R.string.transition));
        activity.startActivity(IntentManager.goToArtist(eventShowArtistDetail.getArtist()), options.toBundle());
    }


    @Subscribe
    public void onEvent(LocalAlbumSelectEvent localAlbumSelectEvent) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, localAlbumSelectEvent.getView(), activity.getString(R.string.transition));
        activity.startActivity(IntentManager.goToAlbum(localAlbumSelectEvent.getModel()), options.toBundle());
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void destroy() {

    }
}
