package com.joxad.zikobot.app.alarm;

import android.os.Bundle;

import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.FragmentAlarmsBinding;

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
