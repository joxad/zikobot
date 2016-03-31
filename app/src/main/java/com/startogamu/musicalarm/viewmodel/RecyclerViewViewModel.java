package com.startogamu.musicalarm.viewmodel;

import android.databinding.ObservableArrayList;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;

/**
 * Created by josh on 31/03/16.
 */
public interface RecyclerViewViewModel<T> extends ViewModel {

    public ObservableArrayList<T> getItems();

    public ItemBinder<T> getItemsBinder();
}
