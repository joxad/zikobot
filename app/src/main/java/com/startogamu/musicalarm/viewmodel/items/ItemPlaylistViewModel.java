package com.startogamu.musicalarm.viewmodel.items;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.startogamu.musicalarm.model.spotify.Item;
import com.startogamu.musicalarm.viewmodel.ViewModel;

/**
 * Created by josh on 26/03/16.
 */
public class ItemPlaylistViewModel extends BaseObservable implements ViewModel {



    private Item item;
    private Context context;

    public ItemPlaylistViewModel(Context context, Item item) {
        this.item = item;
        this.context = context;
    }

    public void setItem(Item item) {
        this.item = item;
        notifyChange();
    }

    /***
     * Click event of an item of alarm
     * @param view
     */
    public void onItemClick(View view) {

    }


    @Bindable
    public String getName() {
        return item.getName();
    }


    @Override
    public void onDestroy() {

    }
}
