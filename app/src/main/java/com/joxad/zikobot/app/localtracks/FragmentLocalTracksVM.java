package com.joxad.zikobot.app.localtracks;

import android.Manifest;
import android.databinding.ObservableArrayList;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.core.viewutils.EndlessRecyclerViewScrollListener;
import com.joxad.zikobot.app.databinding.FragmentLocalTracksBinding;
import com.joxad.zikobot.app.home.event.EventAskPermissionStorage;
import com.joxad.zikobot.app.home.event.EventPermissionRefresh;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.app.player.event.EventPlayListClicked;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.data.module.localmusic.model.LocalAlbum;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 26/03/16.
 */
public abstract class FragmentLocalTracksVM extends FragmentBaseVM<FragmentLocalTracks, FragmentLocalTracksBinding> {

    private static final String TAG = FragmentLocalTracksVM.class.getSimpleName();
    public ObservableArrayList<TrackVM> items;
    public String zmvMessage;
    @Nullable
    LocalAlbum localAlbum;
    private RxPermissions rxPermissions;

    /***
     * @param fragment
     * @param binding
     */
    public FragmentLocalTracksVM(FragmentLocalTracks fragment, FragmentLocalTracksBinding binding) {
        super(fragment, binding);
    }


    @Override
    public void onCreate() {
        rxPermissions = new RxPermissions(fragment.getActivity());
        Parcelable parcelable = fragment.getArguments().getParcelable(EXTRA.LOCAL_ALBUM);
        localAlbum = parcelable != null ? Parcels.unwrap(parcelable) : null;
        zmvMessage = "";
        items = new ObservableArrayList<>();
        binding.rv.setLayoutManager(new GridLayoutManager(fragment.getContext(), 1));
        binding.rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(binding.rv.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadLocalMusic(15, totalItemsCount);
            }
        });
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        if (!rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            updateMessage(fragment.getString(R.string.permission_local));
            binding.zmv.setOnClickListener(v -> askPermission());
            return;
        } else {
            binding.zmv.setVisibility(View.GONE);
        }
        items.clear();
        loadLocalMusic(15, 0);
    }

    @Subscribe
    public void onEvent(EventPlayListClicked eventPlayListClicked) {
        EventBus.getDefault().post(new EventAddList(items));
    }


    @Subscribe
    public void onEvent(EventPermissionRefresh eventPermissionRefresh) {
        initData();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /***
     * Method to ask storage perm
     */
    private void askPermission() {
        EventBus.getDefault().post(new EventAskPermissionStorage());
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
        binding.zmv.setVisibility(View.VISIBLE);
        zmvMessage = string;
        binding.zmv.setZmvMessage(zmvMessage);
    }


    public abstract ItemView getItemView();

}
