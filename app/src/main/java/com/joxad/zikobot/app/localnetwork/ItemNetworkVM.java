package com.joxad.zikobot.app.localnetwork;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.app.core.event.EventSelectItemNetwork;

import org.greenrobot.eventbus.EventBus;
import org.videolan.libvlc.Media;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
    public void onCreate() {

    }

    public void onSelect(View view) {
        EventBus.getDefault().post(new EventSelectItemNetwork(model));
    }

    @Bindable
    public String getName() {
        String decoded = null;
        try {
            String title= model.getTitle();
            if (title.startsWith("/")) {
                title = title.substring(1);
            }
            decoded = URLDecoder.decode(title, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return decoded;
    }

    @Bindable
    public String getType() {
        return "Type : " + model.getMedia().getType();
    }

    @Bindable
    public String getTracksCount() {
        return "Total : " + model.getMedia().getTrackCount();
    }


}
