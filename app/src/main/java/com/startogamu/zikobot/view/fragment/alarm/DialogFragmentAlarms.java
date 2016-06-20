package com.startogamu.zikobot.view.fragment.alarm;

import com.f2prateek.dart.henson.Bundler;
import com.joxad.easydatabinding.fragment.DialogFragmentBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.DialogFragmentAlarmsBinding;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.viewmodel.fragment.alarm.DialogFragmentAlarmsVM;

import org.parceler.Parcels;

/**
 * Created by josh on 20/06/16.
 */
public class DialogFragmentAlarms extends DialogFragmentBase<DialogFragmentAlarmsBinding, DialogFragmentAlarmsVM> {


    public static final String TAG = DialogFragmentAlarms.class.getSimpleName();

    public static DialogFragmentAlarms newInstance(Track track) {
        DialogFragmentAlarms fragment = new DialogFragmentAlarms();
        fragment.setArguments(Bundler.create().put(EXTRA.TRACK, Parcels.wrap(track)).get());
        return fragment;
    }


    @Override
    public int data() {
        return BR.fragmentAlarmsVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.dialog_fragment_alarms;
    }

    @Override
    public DialogFragmentAlarmsVM baseFragmentVM(DialogFragmentAlarmsBinding binding) {
        return new DialogFragmentAlarmsVM(this, binding);
    }

}
