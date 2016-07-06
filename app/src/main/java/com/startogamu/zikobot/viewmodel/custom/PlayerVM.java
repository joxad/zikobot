package com.startogamu.zikobot.viewmodel.custom;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.joxad.easydatabinding.base.IVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.TrackChangeEvent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToCurrent;
import com.startogamu.zikobot.core.event.player.EventPlayTrack;
import com.startogamu.zikobot.databinding.ViewPlayerBinding;
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

    private final ViewPlayerBinding binding;
    public ItemView itemView = ItemView.of(BR.trackVM, R.layout.item_track);
    public ItemView itemViewPlayer = ItemView.of(BR.trackVM, R.layout.view_player_current);

    private final Context context;


    private boolean manualChange = false;
    public PlayerVM(Context context, ViewPlayerBinding binding) {
        this.context = context;
        this.binding = binding;
        init();
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        Injector.INSTANCE.playerComponent().inject(this);
        binding.vpPlayer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                manualChange = true;
                EventBus.getDefault().post(new EventPlayTrack(Injector.INSTANCE.playerComponent().manager().trackVMs().get(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Subscribe
    public void onReceive(TrackChangeEvent trackChangeEvent) {
        if (manualChange) {
            manualChange=false;
            return;
        }
        notifyChange();
        binding.vpPlayer.getAdapter().notifyDataSetChanged();
        binding.vpPlayer.setCurrentItem(Injector.INSTANCE.playerComponent().manager().getCurrentSong(), true);
    }


    @Subscribe
    public void onReceive(EventAddTrackToCurrent trackChangeEvent) {
        notifyChange();
        binding.vpPlayer.getAdapter().notifyDataSetChanged();

    }


    /***
     * @param view
     */
    public void onPlayPauseClicked(View view) {
        Injector.INSTANCE.playerComponent().manager().playOrResume();
        notifyChange();
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }


    @Bindable
    public boolean isPlaying() {
        return Injector.INSTANCE.playerComponent().manager().isPlaying();
    }

    @Bindable
    public int getImageState() {
        if (Injector.INSTANCE.playerComponent().manager().isPlaying()) {
            return R.drawable.ic_pause;
        } else {
            return R.drawable.ic_play_arrow;
        }
    }


    @Bindable
    public ArrayList<TrackVM> getTrackVMs() {
        return Injector.INSTANCE.playerComponent().manager().trackVMs();
    }

    @Bindable
    public boolean getIsEmpty(){
        return Injector.INSTANCE.playerComponent().manager().trackVMs().isEmpty();
    }

}
