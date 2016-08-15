package com.startogamu.zikobot.view.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.startogamu.zikobot.databinding.ItemAlarmBinding;
import com.startogamu.zikobot.viewmodel.base.AlarmVM;

import java.util.Calendar;

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
        ((ItemAlarmBinding)binding).viewDays.tvMonday.setSelected(item.isDayActive(Calendar.MONDAY));
        ((ItemAlarmBinding)binding).viewDays.tvTuesday.setSelected(item.isDayActive(Calendar.TUESDAY));
        ((ItemAlarmBinding)binding).viewDays.tvWednesday.setSelected(item.isDayActive(Calendar.WEDNESDAY));
        ((ItemAlarmBinding)binding).viewDays.tvThursday.setSelected(item.isDayActive(Calendar.THURSDAY));
        ((ItemAlarmBinding)binding).viewDays.tvFriday.setSelected(item.isDayActive(Calendar.FRIDAY));
        ((ItemAlarmBinding)binding).viewDays.tvSaturday.setSelected(item.isDayActive(Calendar.SATURDAY));
        ((ItemAlarmBinding)binding).viewDays.tvSunday.setSelected(item.isDayActive(Calendar.SUNDAY));

    }
}