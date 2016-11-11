package com.startogamu.zikobot.localnetwork;

import android.databinding.ObservableArrayList;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventSelectItemNetwork;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentLocalNetworkBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.util.MediaBrowser;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 29/08/16.
 */
public class FragmentLocalNetworkVM extends FragmentBaseVM<FragmentLocalNetwork, FragmentLocalNetworkBinding> implements MediaBrowser.EventListener {
    protected MediaBrowser mMediaBrowser;
    public ItemView itemNetwork = ItemView.of(BR.itemNetworkVM, R.layout.item_network);
    public ObservableArrayList<ItemNetworkVM> networkItems;
    private String path;
    private String current;
    /***
     * @param fragment
     * @param binding
     */
    public FragmentLocalNetworkVM(FragmentLocalNetwork fragment, FragmentLocalNetworkBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void onCreate() {
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
        if (title!=null && current!=null) {
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
