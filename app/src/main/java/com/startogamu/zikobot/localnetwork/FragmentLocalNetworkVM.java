package com.startogamu.zikobot.localnetwork;

import android.databinding.ObservableArrayList;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;

import com.github.ayvazj.breadcrumblayout.BreadcrumbLayout;
import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventSelectItemNetwork;
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
    public String currentMedia = "Root";

    /***
     * @param fragment
     * @param binding
     */
    public FragmentLocalNetworkVM(FragmentLocalNetwork fragment, FragmentLocalNetworkBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {
        binding.rv.setLayoutManager(new GridLayoutManager(fragment.getContext(), 1));
        binding.rv.setNestedScrollingEnabled(false);
        LibVLC libVLC = new LibVLC();
        networkItems = new ObservableArrayList<ItemNetworkVM>();

        if (mMediaBrowser == null)
            mMediaBrowser = new MediaBrowser(libVLC, this);
        else
            mMediaBrowser.changeEventListener(this);
        discover();
        binding.breadcrumbLayout.addCrumb(binding.breadcrumbLayout.newCrumb().setText("Root"));
        // listen for selections

        binding.breadcrumbLayout.setOnBreadcrumbSelectedListener(new BreadcrumbLayout.OnBreadcrumbSelectedListener() {
            @Override
            public void onBreadcrumbSelected(BreadcrumbLayout.Breadcrumb crumb) {
                if (crumb.getText().toString().contains("Root")) {
                    discover();
                } else {

                    int position = crumb.getPosition();
                    String path = "";
                    for (int i = 1; i <= position; i++) {
                        path += binding.breadcrumbLayout.getCrumbAt(i).getText();
                    }
                    currentMedia = path;
                    discover(Uri.parse(path));
                }
            }

            @Override
            public void onBreadcrumbUnselected(BreadcrumbLayout.Breadcrumb crumb) {

            }

            @Override
            public void onBreadcrumbReselected(BreadcrumbLayout.Breadcrumb crumb) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    public void discover() {
        networkItems.clear();
        currentMedia = "Root";
        mMediaBrowser.discoverNetworkShares();
    }

    public void discover(Uri uri) {
        networkItems.clear();
        mMediaBrowser.browse(uri, MediaBrowser.Flag.Interact);
    }


    @Subscribe
    public void onReceive(EventSelectItemNetwork eventSelectItemNetwork) {
        currentMedia = eventSelectItemNetwork.getModel().getMedia().getUri().toString();

        binding.breadcrumbLayout.addCrumb(binding.breadcrumbLayout.newCrumb()
                .setText(eventSelectItemNetwork.getModel().getTitle()));

        discover(eventSelectItemNetwork.getModel().getMedia().getUri());
    }

    @Override
    public void onMediaAdded(int i, Media media) {
        Logger.d("Added" + media.toString() + media.getUri().toString());
        String title = "";
        if (!currentMedia.equals(""))
            title = media.getUri().toString().replace(currentMedia, "");
        else
            title = media.getUri().toString();
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

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void destroy() {

    }
}
