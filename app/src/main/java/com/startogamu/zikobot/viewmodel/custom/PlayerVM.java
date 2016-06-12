package com.startogamu.zikobot.viewmodel.custom;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.view.View;

import com.joxad.easydatabinding.base.IVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.player.EventPlayTrack;
import com.startogamu.zikobot.module.alarm.model.Track;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.mock.Mock;
import com.startogamu.zikobot.viewmodel.base.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by josh on 08/06/16.
 */
public class PlayerVM extends BaseObservable implements IVM {

    public ObservableArrayList<Track> tracks;


    @Bindable
    public boolean isPlaying = false;
    private final Context context;
    public TrackVM trackVM;

    public PlayerVM(Context context) {
        this.context = context;
        init();
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        Injector.INSTANCE.playerComponent().inject(this);
        trackVM = new TrackVM(context, Mock.trackPlayer(context));
        tracks = new ObservableArrayList<>();
    }

    @Subscribe
    public void onReceive(EventPlayTrack changeEvent) {
        trackVM.updateTrack(changeEvent.getTrack());
        isPlaying = true;
        notifyChange();
        Injector.INSTANCE.playerComponent().manager().playTrack(changeEvent.getTrack());
        notifyChange();
    }

    /***
     * @param view
     */
    public void onPlayPauseClicked(View view) {
        isPlaying = !isPlaying;
        if (isPlaying) {
            Injector.INSTANCE.playerComponent().manager().resume();
        } else {
            Injector.INSTANCE.playerComponent().manager().pause();
        }
        notifyChange();
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

    @Bindable
    public int getImageState() {
        if (isPlaying) {
            return R.drawable.ic_pause;
        } else {
            return R.drawable.ic_play_arrow;
        }
    }

}
