package com.joxad.zikobot.app.album;

import android.Manifest;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.core.viewutils.EndlessRecyclerViewScrollListener;
import com.joxad.zikobot.app.databinding.FragmentLocalAlbumsBinding;
import com.joxad.zikobot.app.home.event.EventAskPermissionStorage;
import com.joxad.zikobot.app.home.event.EventPermissionRefresh;
import com.joxad.zikobot.data.event.EventShowArtistDetail;
import com.joxad.zikobot.data.model.Album;
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.data.module.localmusic.model.LocalAlbum;
import com.joxad.zikobot.data.module.localmusic.model.LocalArtist;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 06/06/16.
 */
public class FragmentLocalAlbumsVM extends FragmentBaseVM<FragmentLocalAlbums, FragmentLocalAlbumsBinding> {

    @Nullable
    LocalArtist localArtist;

    public static final String TAG = "FragmentLocalAlbumsVM";
    public String zmvMessage;

    private RxPermissions rxPermissions;
    public ItemView itemView = ItemView.of(BR.albumVM, R.layout.item_album);
    public ObservableArrayList<AlbumVM> items;

    /***
     * @param fragment
     * @param binding
     */
    public FragmentLocalAlbumsVM(FragmentLocalAlbums fragment, FragmentLocalAlbumsBinding binding) {
        super(fragment, binding);
    }


    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        Parcelable parcelable = fragment.getArguments().getParcelable(EXTRA.LOCAL_ARTIST);
        localArtist = parcelable != null ? Parcels.unwrap(parcelable) : null;
        items = new ObservableArrayList<>();
        zmvMessage = "";
        binding.rv.setLayoutManager(new GridLayoutManager(fragment.getContext(), 2));
        binding.rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(binding.rv.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadLocalMusic(15, totalItemsCount);
            }
        });
        rxPermissions = new RxPermissions(fragment.getActivity());
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
    public void onReceive(EventPermissionRefresh eventPermission) {
        initData();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onClick(View view) {
        EventBus.getDefault().post(new EventShowArtistDetail(null, view));
    }


    /***
     * Load the local music
     */
    public void loadLocalMusic(int limit, int offset) {


        LocalMusicManager.getInstance().getLocalAlbums(limit, offset, localArtist != null ? localArtist.getName() : null, null).subscribe(localAlbums -> {
            Log.d(TAG, "" + localAlbums.size());
            for (LocalAlbum localAlbum : localAlbums) {
                items.add(new AlbumVM(fragment.getContext(), Album.from(localAlbum)));
            }
            if (localAlbums.isEmpty() && offset == 0) {
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

    /***
     * Method to ask storage perm
     */
    private void askPermission() {
        EventBus.getDefault().post(new EventAskPermissionStorage());
    }

}
