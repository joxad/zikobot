package com.startogamu.zikobot;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.databinding.PlayerViewBottomBinding;
import com.startogamu.zikobot.player.PlayerVM;

/**
 * Created by Jocelyn on 15/08/2017.
 */

public abstract class ABasePlayerActivityVM<A extends ActivityBase, B extends ViewDataBinding> extends ActivityBaseVM<A, B> {

    public PlayerVM playerVM;

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
