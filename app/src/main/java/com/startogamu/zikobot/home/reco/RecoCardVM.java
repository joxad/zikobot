package com.startogamu.zikobot.home.reco;

import android.content.Context;
import android.databinding.Bindable;

import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.data.module.recommandations.Reco;

/**
 * Created by Jocelyn on 11/11/2017.
 */

public class RecoCardVM extends BaseVM<Reco> {

    public RecoCardContentVM recoCardContentVM;

    /***
     * @param context
     * @param model
     */
    public RecoCardVM(Context context, Reco model) {
        super(context, model);
    }

    @Override
    public void onCreate() {
        recoCardContentVM = new RecoCardContentVM(context);
    }


    @Bindable
    public String getTitle() {
        return "Title";
    }
}
