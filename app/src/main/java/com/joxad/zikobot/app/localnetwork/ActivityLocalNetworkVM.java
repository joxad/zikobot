package com.joxad.zikobot.app.localnetwork;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.app.core.fragmentmanager.FragmentManager;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.databinding.ActivityLocalNetworkBinding;
import com.joxad.zikobot.data.event.EventSelectItemNetwork;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by josh on 10/10/16.
 */

public class ActivityLocalNetworkVM extends ActivityBaseVM<ActivityLocalNetwork, ActivityLocalNetworkBinding> {
    private String mediaPath;
    private Queue<String> medias;

    /***
     * @param activity
     * @param binding
     */
    public ActivityLocalNetworkVM(ActivityLocalNetwork activity, ActivityLocalNetworkBinding binding,@Nullable Bundle saved) {
        super(activity, binding,saved);
    }

    @Override
    public void onCreate(@Nullable Bundle saved) {
        medias = new LinkedList<>();
        initToolbar();
        initFragment();
    }

    private void initToolbar() {
        mediaPath = activity.getIntent().getStringExtra(EXTRA.MEDIA);
        medias.add(mediaPath);
//        binding.toolbar.initToolbar(new BreadcrumbToolbarItem(mediaPath));
//        binding.toolbar.setBreadcrumbToolbarListener(new BreadcrumbToolbar.BreadcrumbToolbarListener() {
//            @Override
//            public void onBreadcrumbToolbarItemPop(int stackSize) {
//                FragmentManager.pop(activity);
//                if (stackSize == 1) {
//                    activity.finish();
//                }
//            }
//
//            @Override
//            public void onBreadcrumbToolbarEmpty() {
//                Logger.d("Empty");
//            }
//
//            @Override
//            public String getFragmentName() {
//                return medias.element();
//
//            }
//        });
    }

    private void initFragment() {
        FragmentManager.replaceFragment(activity, FragmentLocalNetwork.newInstance(null, mediaPath), true, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onReceive(EventSelectItemNetwork eventSelectItemNetwork) {

        String media = eventSelectItemNetwork.getModel().getTitle();
        media = media.replace(medias.element(), "");
        medias.add(media);
//        binding.toolbar.addItem(new BreadcrumbToolbarItem(media));
        String path = "";
        for (String s : medias) {
            path += s;
        }
        FragmentManager.replaceFragment(activity, FragmentLocalNetwork.newInstance(null, path), false, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
