package com.startogamu.zikobot.home.reco;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Jocelyn on 11/11/2017.
 */

public abstract class RecoCardContentVM<T> extends BaseObservable {


    public ItemBinding itemBinding = ItemBinding.of(BR.artistVM, R.layout.artist_item);
    public ObservableArrayList<T> items;
    protected Context context;

    /***
     * @param context
     */
    public RecoCardContentVM(Context context) {
        this.context = context;
    }

    public void onCreate() {
        items = new ObservableArrayList<>();
    }


}
