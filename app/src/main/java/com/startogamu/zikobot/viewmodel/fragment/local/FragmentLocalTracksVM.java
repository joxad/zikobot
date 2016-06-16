package com.startogamu.zikobot.viewmodel.fragment.local;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.navigation_manager.EventCollapseToolbar;
import com.startogamu.zikobot.core.event.navigation_manager.EventTabBars;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventPlayListClicked;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.REQUEST;
import com.startogamu.zikobot.databinding.FragmentLocalTracksBinding;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalTrack;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalTracks;
import com.startogamu.zikobot.viewmodel.base.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 26/03/16.
 */
public abstract class FragmentLocalTracksVM extends FragmentBaseVM<FragmentLocalTracks, FragmentLocalTracksBinding> {

    @Nullable
    @InjectExtra(EXTRA.LOCAL_ALBUM)
    LocalAlbum localAlbum;
    private static final String TAG = FragmentLocalTracksVM.class.getSimpleName();
    public ObservableArrayList<TrackVM> items;

    public ObservableBoolean showZmvMessage;

    public String zmvMessage;

    /***
     * @param fragment
     * @param binding
     */
    public FragmentLocalTracksVM(FragmentLocalTracks fragment, FragmentLocalTracksBinding binding) {
        super(fragment, binding);
    }


    @Override
    public void init() {
        Dart.inject(this, fragment.getArguments());
        showZmvMessage = new ObservableBoolean(false);
        zmvMessage = "";
        items = new ObservableArrayList<>();
        Injector.INSTANCE.contentResolverComponent().init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                askPermission();
            } else {
                loadLocalMusic();
            }
        } else {
            loadLocalMusic();
            // continue with your code
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        if (localAlbum != null) {
            EventBus.getDefault().post(new EventCollapseToolbar(localAlbum.getName(), localAlbum.getImage()));
            EventBus.getDefault().post(new EventTabBars(false, TAG));
        } else {
            EventBus.getDefault().post(new EventCollapseToolbar(null, null));
            EventBus.getDefault().post(new EventTabBars(true, TAG));
        }
    }


    @Subscribe
    public void onEvent(EventPlayListClicked eventPlayListClicked){
        EventBus.getDefault().post(new EventAddTrackToPlayer(items));
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    /***
     * Method to ask storage perm
     */
    private void askPermission() {
        ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST.PERMISSION_STORAGE_TRACKS);

    }

    /***
     * Load the local music
     */
    public void loadLocalMusic() {
        Injector.INSTANCE.contentResolverComponent().localMusicManager().getLocalTracks(localAlbum != null ? localAlbum.getId() : -1).subscribe(localTracks -> {
            Log.d(TAG, "" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                items.add(new TrackVM(fragment.getContext(), Track.from(localTrack)));
            }
            if (localTracks.isEmpty()) {
                updateMessage(fragment.getString(R.string.no_music));
            } else {
                showZmvMessage.set(false);
            }
        }, throwable -> {
            updateMessage(fragment.getString(R.string.no_music));

        });
    }

    /***
     * Update t
     *
     * @param string
     */
    private void updateMessage(String string) {
        showZmvMessage.set(true);
        zmvMessage = string;
        binding.zmv.setZmvMessage(zmvMessage);
    }

    @Override
    public void destroy() {

    }

    public abstract ItemView getItemView();

}
