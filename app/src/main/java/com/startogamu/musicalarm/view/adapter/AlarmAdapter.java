package com.startogamu.musicalarm.view.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.startogamu.musicalarm.databinding.ItemAlarmBinding;
import com.startogamu.musicalarm.viewmodel.base.AlarmVM;

import me.tatarka.bindingcollectionadapter.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter.ItemViewArg;

public class AlarmAdapter<T> extends BindingRecyclerViewAdapter<AlarmVM> {
    public AlarmAdapter(@NonNull ItemViewArg<AlarmVM> arg) {
        super(arg);
    }

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup viewGroup) {
        ViewDataBinding binding = super.onCreateBinding(inflater, layoutId, viewGroup);
        return binding;
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutId, int position, AlarmVM item) {
        super.onBindBinding(binding, bindingVariable, layoutId, position, item);
       RxCompoundButton.checkedChanges( ((ItemAlarmBinding) binding).swActivated).subscribe(item::updateStatus);
    }
}