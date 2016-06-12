package com.startogamu.zikobot.viewmodel.custom;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.IVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.TrackChangeEvent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventPlayTrack;
import com.startogamu.zikobot.module.alarm.model.Track;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.viewmodel.base.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 08/06/16.
 */
public class PlayerVM extends BaseObservable implements IVM {

    public ItemView itemView = ItemView.of(BR.trackVM, R.layout.item_track);

    @Bindable
    public boolean isPlaying = false;
    private final Context context;


    public PlayerVM(Context context) {
        this.context = context;
        init();
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        Injector.INSTANCE.playerComponent().inject(this);
    }

    @Subscribe
    public void onReceive(EventPlayTrack changeEvent) {
        isPlaying = true;
        Injector.INSTANCE.playerComponent().manager().playTrack(changeEvent.getTrack());
        notifyChange();
    }


    @Subscribe
    public void onReceive(TrackChangeEvent trackChangeEvent) {
        notifyChange();
    }


    @Subscribe
    public void onReceive(EventAddTrackToPlayer eventAddTrackToPlayer) {
        ArrayList<Track> tracks = new ArrayList<>();
        for (TrackVM trackVM : eventAddTrackToPlayer.getItems()) {
            tracks.add(trackVM.getModel());
        }
        isPlaying = true;
        Injector.INSTANCE.playerComponent().manager().playTracks(tracks);
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
        return Injector.INSTANCE.playerComponent().manager().getCurrentImage();
    }

    @Bindable
    public String getTrackName() {
        return Injector.INSTANCE.playerComponent().manager().getCurrentTrackName();
    }

    @Bindable
    public String getArtistName() {
        return Injector.INSTANCE.playerComponent().manager().getCurrentArtisteName();
    }

    @Bindable
    public int getImageState() {
        if (isPlaying) {
            return R.drawable.ic_pause;
        } else {
            return R.drawable.ic_play_arrow;
        }
    }


    @Bindable
    public ArrayList<TrackVM> getTrackVMs() {
        return Injector.INSTANCE.playerComponent().manager().trackVMs();
    }


}
