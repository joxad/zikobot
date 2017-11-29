package com.startogamu.zikobot.home.reco;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.joxad.zikobot.data.module.recommandations.Reco;

/**
 * Created by Jocelyn on 11/11/2017.
 */

public abstract class RecoCardVM extends BaseObservable {

    private Context context;
    private Reco model;

    /***
     * @param context
     * @param model
     */
    public RecoCardVM(Context context, Reco model) {
        this.context = context;
        this.model = model;
    }

    public void onCreate() {
        recoCardContentVM().onCreate();
    }


    @Bindable
    public String getTitle() {
        return model.getTitle();
    }

    public abstract RecoCardContentVM recoCardContentVM();
}
