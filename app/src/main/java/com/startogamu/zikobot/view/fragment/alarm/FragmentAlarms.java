package com.startogamu.zikobot.view.fragment.alarm;

import android.os.Bundle;

import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentAlarmsBinding;
import com.startogamu.zikobot.viewmodel.fragment.alarm.FragmentAlarmsVM;

/***
 * List the alarms
 */
public class FragmentAlarms extends FragmentBase<FragmentAlarmsBinding, FragmentAlarmsVM> {

    private String TAG = FragmentAlarms.class.getSimpleName();

    public static FragmentAlarms newInstance() {
        Bundle args = new Bundle();
        FragmentAlarms fragment = new FragmentAlarms();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentAlarmsVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_alarms;
    }

    @Override
    public FragmentAlarmsVM baseFragmentVM(FragmentAlarmsBinding bindingé) {
        return  new FragmentAlarmsVM(this, binding);
    }
}
