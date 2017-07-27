package com.joxad.zikobot.app.artist;

import android.Manifest;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.viewutils.EndlessRecyclerViewScrollListener;
import com.joxad.zikobot.app.databinding.FragmentLocalArtistsBinding;
import com.joxad.zikobot.app.home.event.EventAskPermissionStorage;
import com.joxad.zikobot.app.home.event.EventPermissionRefresh;
import com.joxad.zikobot.data.db.model.Artist;
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.data.module.localmusic.model.LocalArtist;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by josh on 06/06/16.
 */
public class FragmentLocalArtistsVM extends FragmentBaseVM<FragmentLocalArtists, FragmentLocalArtistsBinding> {

    private static final String TAG = FragmentLocalArtistsVM.class.getSimpleName();
    public ItemBinding itemView = ItemBinding.of(BR.artistVM, R.layout.item_artist);
    public ObservableArrayList<ArtistVM> items;
    public String zmvMessage;
    private RxPermissions rxPermissions;

    /***
     * @param fragment
     * @param binding
     */
    public FragmentLocalArtistsVM(FragmentLocalArtists fragment, FragmentLocalArtistsBinding binding,@Nullable Bundle saved) {
        super(fragment, binding,saved);
    }

    @Override
    public void onCreate(@Nullable Bundle saved) {
        rxPermissions = new RxPermissions(fragment.getActivity());
        EventBus.getDefault().register(this);
        items = new ObservableArrayList<>();
        binding.rv.setLayoutManager(new GridLayoutManager(fragment.getContext(), 2));
        binding.rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(binding.rv.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadLocalMusic(15, totalItemsCount);
            }
        });
        initData();
    }

    void initData() {
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
    public void onReceive(EventPermissionRefresh eventPermission) {
        initData();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /***
     * Load the local music
     */
    public void loadLocalMusic(int limit, int offset) {

        LocalMusicManager.getInstance().getLocalArtists(limit, offset, null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(localArtists -> {
            Log.d(TAG, "" + localArtists.size());
            for (LocalArtist localArtist : localArtists) {
                items.add(new ArtistVM(fragment.getActivity(), Artist.from(localArtist)));
            }
            if (localArtists.isEmpty() && offset == 0) {
                updateMessage(fragment.getString(R.string.no_music));
            }
        }, throwable -> {
            if (offset == 0)
                updateMessage(fragment.getString(R.string.no_music));

        });
    }


    /***
     * Method to ask storage perm
     */
    private void askPermission() {
        EventBus.getDefault().post(new EventAskPermissionStorage());
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

}
