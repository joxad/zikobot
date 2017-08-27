package com.startogamu.zikobot;

import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.databinding.PlayerViewBottomBinding;
import com.startogamu.zikobot.home.track.TrackVM;
import com.startogamu.zikobot.player.PlayerVM;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Jocelyn on 15/08/2017.
 */

public abstract class ABasePlayerActivityVM<A extends ActivityBase, B extends ViewDataBinding> extends ActivityBaseVM<A, B> {

    public PlayerVM playerVM;
    public ObservableArrayList<TrackVM> items;
    public ItemBinding<TrackVM> itemBinding = ItemBinding.of(BR.trackVM, R.layout.track_item);

    /***

     * @param activity
     * @param binding
     * @param savedInstance
     */
    public ABasePlayerActivityVM(A activity, B binding, @Nullable Bundle savedInstance) {
        super(activity, binding, savedInstance);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstance) {
        playerVM = new PlayerVM(activity, playerBinding());
        items = new ObservableArrayList<>();
    }

    protected abstract PlayerViewBottomBinding playerBinding();

    @Override
    public void onResume() {
        super.onResume();
        playerVM.onResume();
    }

    @Override
    protected boolean onBackPressed() {
        return playerVM.onBackPressed();
    }

    public abstract void playAll(View view);

    @Override
    public void onPause() {
        super.onPause();
        playerVM.onPause();
    }
}
