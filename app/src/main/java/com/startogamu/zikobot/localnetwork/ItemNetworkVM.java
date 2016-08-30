package com.startogamu.zikobot.localnetwork;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.core.event.EventSelectItemNetwork;

import org.greenrobot.eventbus.EventBus;
import org.videolan.libvlc.Media;

/**
 * Created by josh on 29/08/16.
 */
public class ItemNetworkVM extends BaseVM<ItemNetwork> {

    /***
     * @param context
     * @param model
     */
    public ItemNetworkVM(Context context, ItemNetwork model) {
        super(context, model);
    }

    @Override
    public void init() {

    }

    public void onSelect(View view) {
        EventBus.getDefault().post(new EventSelectItemNetwork(model));
    }

    @Bindable
    public String getName() {
        return model.getTitle();
    }

    @Bindable
    public String getType() {
        return "Type : " + model.getMedia().getType();
    }

    @Bindable
    public String getTracksCount() {
        return "Total : " + model.getMedia().getTrackCount();
    }

    @Override
    public void destroy() {

    }
}
