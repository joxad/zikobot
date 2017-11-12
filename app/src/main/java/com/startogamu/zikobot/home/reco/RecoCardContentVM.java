package com.startogamu.zikobot.home.reco;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Jocelyn on 11/11/2017.
 */

public class RecoCardContentVM<T> extends BaseObservable {


    public ObservableArrayList<T> items;
    public ItemBinding<T> itemBinding;

    /***
     * @param context
     */
    public RecoCardContentVM(Context context) {
    }

    public void onCreate() {
        items = new ObservableArrayList<>();
    }


}
