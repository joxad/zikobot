package com.joxad.zikobot.app.album;

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
import android.view.View;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.core.event.EventShowArtistDetail;
import com.joxad.zikobot.app.core.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.app.core.module.localmusic.model.LocalAlbum;
import com.joxad.zikobot.app.core.module.localmusic.model.LocalArtist;
import com.joxad.zikobot.app.core.utils.Constants;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.viewutils.EndlessRecyclerViewScrollListener;
import com.joxad.zikobot.app.databinding.FragmentLocalAlbumsBinding;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 06/06/16.
 */
public class FragmentLocalAlbumsVM extends FragmentBaseVM<FragmentLocalAlbums, FragmentLocalAlbumsBinding> {

    @Nullable
    LocalArtist localArtist;

    public static final String TAG = "FragmentLocalAlbumsVM";
    public ObservableBoolean showZmvMessage;

    public String zmvMessage;

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
        Parcelable parcelable = fragment.getArguments().getParcelable(EXTRA.LOCAL_ARTIST);
        localArtist = parcelable != null ? Parcels.unwrap(parcelable) : null;
        items = new ObservableArrayList<>();
        showZmvMessage = new ObservableBoolean(false);
        zmvMessage = "";
        binding.rv.setLayoutManager(new GridLayoutManager(fragment.getContext(), 2));
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
                items.add(new AlbumVM(fragment.getContext(), localAlbum));
            }
            if (localAlbums.isEmpty() && offset == 0) {
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

    /***
     * Method to ask storage perm
     */
    private void askPermission() {
        ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_STORAGE_ALBUM);

    }

  }
