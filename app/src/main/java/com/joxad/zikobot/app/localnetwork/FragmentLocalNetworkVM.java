package com.joxad.zikobot.app.localnetwork;

import android.databinding.ObservableArrayList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.databinding.FragmentLocalNetworkBinding;
import com.joxad.zikobot.data.db.model.ItemNetwork;
import com.orhanobut.logger.Logger;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.util.MediaBrowser;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by josh on 29/08/16.
 */
public class FragmentLocalNetworkVM extends FragmentBaseVM<FragmentLocalNetwork, FragmentLocalNetworkBinding> implements MediaBrowser.EventListener {
    public ItemBinding itemNetwork = ItemBinding.of(BR.itemNetworkVM, R.layout.item_network);
    public ObservableArrayList<ItemNetworkVM> networkItems;
    protected MediaBrowser mMediaBrowser;
    private String path;
    private String current;

    /***
     * @param fragment
     * @param binding
     */
    public FragmentLocalNetworkVM(FragmentLocalNetwork fragment, FragmentLocalNetworkBinding binding,@Nullable Bundle saved) {
        super(fragment, binding,saved);
    }

    @Override
    public void onCreate(@Nullable Bundle saved) {
        binding.rv.setLayoutManager(new GridLayoutManager(fragment.getContext(), 1));
        binding.rv.setNestedScrollingEnabled(false);
        LibVLC libVLC = new LibVLC();
        networkItems = new ObservableArrayList<>();
        current = fragment.getArguments().getString(EXTRA.FRAGMENT_TITLE);
        path = fragment.getArguments().getString(EXTRA.MEDIA);
        if (mMediaBrowser == null)
            mMediaBrowser = new MediaBrowser(libVLC, this);
        discover(path);


    }


    public void discover(String path) {

        networkItems.clear();
        if (path == null) {
            mMediaBrowser.discoverNetworkShares();
        } else {
            mMediaBrowser.browse(Uri.parse(path), MediaBrowser.Flag.Interact);

        }
    }


    @Override
    public void onMediaAdded(int i, Media media) {
        Logger.d("Added" + media.toString() + media.getUri().toString());
        String title = media.getUri().toString();
        if (title != null && current != null) {
            title = title.replace(current, "");
        }
        networkItems.add(new ItemNetworkVM(fragment.getContext(), new ItemNetwork(title, media)));
    }

    @Override
    public void onMediaRemoved(int i, Media media) {
        Logger.d("Removed " + media.toString());

    }

    @Override
    public void onBrowseEnd() {
        Logger.d("BROWSE END");

    }

}
