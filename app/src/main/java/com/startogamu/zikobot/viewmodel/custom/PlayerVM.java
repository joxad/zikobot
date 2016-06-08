package com.startogamu.zikobot.viewmodel.custom;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.joxad.easydatabinding.base.IVM;
import com.startogamu.zikobot.core.event.TrackChangeEvent;
import com.startogamu.zikobot.viewmodel.base.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by josh on 08/06/16.
 */
public class PlayerVM extends BaseObservable implements IVM {


    private final Context context;
    public TrackVM trackVM;

    public PlayerVM(Context context) {
        this.context = context;
        init();
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onReceive(TrackChangeEvent changeEvent) {


        trackVM.updateTrack(changeEvent.getTrack());
    }
    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }


    @Bindable
    public String getTrackImage() {
        return trackVM.getImageUrl();
    }

    @Bindable
    public String getTrackName() {
        return trackVM.getName();
    }

    @Bindable
    public String getArtistName() {
        return trackVM.getArtistName();
    }

}
