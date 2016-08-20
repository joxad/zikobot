package com.startogamu.zikobot.viewmodel.fragment.local;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.Constants;
import com.startogamu.zikobot.databinding.FragmentLocalArtistsBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.content_resolver.model.LocalArtist;
import com.startogamu.zikobot.module.zikobot.model.Artist;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalArtists;
import com.startogamu.zikobot.viewmodel.base.ArtistVM;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 06/06/16.
 */
public class FragmentLocalArtistVM extends FragmentBaseVM<FragmentLocalArtists, FragmentLocalArtistsBinding> {

    private static final String TAG = FragmentLocalArtistVM.class.getSimpleName();


    public ItemView itemView = ItemView.of(BR.artistVM, R.layout.item_artist);
    public ObservableArrayList<ArtistVM> items;

    public ObservableBoolean showZmvMessage;

    public String zmvMessage;

    /***
     * @param fragment
     * @param binding
     */
    public FragmentLocalArtistVM(FragmentLocalArtists fragment, FragmentLocalArtistsBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {
        showZmvMessage =new ObservableBoolean(false);
        items = new ObservableArrayList<>();
        Injector.INSTANCE.contentResolverComponent().init(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadLocalMusic();

    }

    /***
     * Load the local music
     */
    public void loadLocalMusic() {
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            updateMessage(fragment.getString(R.string.permission_local));
            binding.zmv.setOnClickListener(v ->askPermission());
            return;
        }
        items.clear();
        Injector.INSTANCE.contentResolverComponent().localMusicManager().getLocalArtists(null).subscribe(localArtists -> {
            Log.d(TAG, "" + localArtists.size());
            for (LocalArtist localArtist : localArtists) {
                items.add(new ArtistVM(fragment.getActivity(), Artist.from(localArtist)));
            }
            if (localArtists.isEmpty()) {
                updateMessage(fragment.getString(R.string.no_music));
            } else {
                showZmvMessage.set(false);
            }
        }, throwable -> {
            updateMessage(fragment.getString(R.string.no_music));

        });
    }


    /***
     * Method to ask storage perm
     */
    private void askPermission() {
        ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_STORAGE_ARTIST);

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
}
