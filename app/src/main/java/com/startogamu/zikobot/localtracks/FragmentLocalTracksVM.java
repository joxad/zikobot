package com.startogamu.zikobot.localtracks;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventPlayListClicked;
import com.startogamu.zikobot.core.utils.Constants;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.viewutils.EndlessRecyclerViewScrollListener;
import com.startogamu.zikobot.databinding.FragmentLocalTracksBinding;

import com.startogamu.zikobot.core.module.localmusic.manager.LocalMusicManager;
import com.startogamu.zikobot.core.module.localmusic.model.LocalAlbum;
import com.startogamu.zikobot.core.module.localmusic.model.LocalTrack;
import com.startogamu.zikobot.core.model.Track;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 26/03/16.
 */
public abstract class FragmentLocalTracksVM extends FragmentBaseVM<FragmentLocalTracks, FragmentLocalTracksBinding> {

    @Nullable
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
    public void onCreate() {
        Parcelable parcelable = fragment.getArguments().getParcelable(EXTRA.LOCAL_ALBUM);
        localAlbum = parcelable != null ? Parcels.unwrap(parcelable) : null;
        showZmvMessage = new ObservableBoolean(false);
        zmvMessage = "";
        items = new ObservableArrayList<>();
        binding.rv.setLayoutManager(new GridLayoutManager(fragment.getContext(), 1));
        binding.rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(binding.rv.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadLocalMusic(15, totalItemsCount);
            }
        });
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            updateMessage(fragment.getString(R.string.permission_local));
            binding.zmv.setOnClickListener(v -> askPermission());
            return;
        }
        items.clear();
        loadLocalMusic(15, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

    }


    @Subscribe
    public void onEvent(EventPlayListClicked eventPlayListClicked) {
        EventBus.getDefault().post(new EventAddTrackToPlayer(items));
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    /***
     * Method to ask storage perm
     */
    private void askPermission() {
        ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_STORAGE_TRACKS);

    }

    /***
     * Load the local music
     */
    public void loadLocalMusic(int limit, int offset) {

        LocalMusicManager.getInstance().getLocalTracks(limit, offset, null, localAlbum != null ? localAlbum.getId() : -1, null)
                .subscribe(localTracks -> {
                    Log.d(TAG, "" + localTracks.size());
                    for (LocalTrack localTrack : localTracks) {
                        items.add(new TrackVM(fragment.getContext(), Track.from(localTrack)));
                    }

                    if (localTracks.isEmpty() && offset == 0) {
                        updateMessage(fragment.getString(R.string.no_music));
                    } else {
                        showZmvMessage.set(false);
                    }
                }, throwable -> {
                    if (offset == 0)
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


    public abstract ItemView getItemView();

}
