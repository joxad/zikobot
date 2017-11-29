package com.startogamu.zikobot.home.reco;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.joxad.zikobot.data.module.recommandations.Reco;

/**
 * Created by Jocelyn on 11/11/2017.
 */

public class RecoCardVM extends BaseObservable {

    private Context context;
    private Reco model;

    public ArtistSimilarRecoCardContentVM recoCardContentVM;
    /***
     * @param context
     * @param model
     */
    public RecoCardVM(Context context, Reco model) {
        this.context = context;
        this.model = model;
    }

    public void onCreate() {
        recoCardContentVM=new ArtistSimilarRecoCardContentVM(context);
        recoCardContentVM.onCreate();
        notifyChange();
    }


    @Bindable
    public String getTitle() {
        return model.getTitle();
    }


}
