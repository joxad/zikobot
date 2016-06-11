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
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.navigation_manager.EventCollapseToolbar;
import com.startogamu.zikobot.core.event.navigation_manager.EventTabBars;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.REQUEST;
import com.startogamu.zikobot.databinding.FragmentLocalAlbumsBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalArtist;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalAlbums;
import com.startogamu.zikobot.viewmodel.base.AlbumVM;

import org.greenrobot.eventbus.EventBus;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 06/06/16.
 */
public class FragmentLocalAlbumsVM extends FragmentBaseVM<FragmentLocalAlbums, FragmentLocalAlbumsBinding> {

    @Nullable
    @InjectExtra(EXTRA.LOCAL_ARTIST)
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
    public void init() {

        Dart.inject(this, fragment.getArguments());
        items = new ObservableArrayList<>();
        showZmvMessage = new ObservableBoolean(false);
        zmvMessage = "";
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
        if (localArtist != null) {
            EventBus.getDefault().post(new EventCollapseToolbar(localArtist.getName(), localArtist.getImage()));
            EventBus.getDefault().post(new EventTabBars(false, TAG));
        } else {
            EventBus.getDefault().post(new EventCollapseToolbar(null, null));
            EventBus.getDefault().post(new EventTabBars(true, TAG));
        }
    }

    /***
     * Load the local music
     */
    public void loadLocalMusic() {
        Injector.INSTANCE.contentResolverComponent().localMusicManager().getLocalAlbums(localArtist != null ? localArtist.getName() : null).subscribe(localAlbums -> {
            Log.d(TAG, "" + localAlbums.size());
            for (LocalAlbum localAlbum : localAlbums) {
                items.add(new AlbumVM(fragment.getContext(), localAlbum));
            }
            if (localAlbums.isEmpty()) {
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

    public void permissionDenied() {
        updateMessage(fragment.getString(R.string.permission_local));
        binding.zmv.setOnClickListener(v -> askPermission());
    }

    /***
     * Method to ask storage perm
     */
    private void askPermission() {
        ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST.PERMISSION_STORAGE);

    }

    @Override
    public void destroy() {

    }
}
