package com.joxad.zikobot.app.alarm;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.joxad.zikobot.app.databinding.ItemAlarmBinding;

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
        ((ItemAlarmBinding) binding).swActivated.setOnClickListener(v -> item.updateStatus(!item.isActivated()));

    }
}