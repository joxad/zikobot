package com.startogamu.zikobot.playlist;

import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.fragment.DialogFragmentBaseVM;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.databinding.DialogPlaylistEditBinding;
import com.startogamu.zikobot.module.zikobot.manager.AlarmManager;
import com.startogamu.zikobot.module.zikobot.model.Alarm;

import org.parceler.Parcels;

/**
 * Created by josh on 16/08/16.
 */
public class DialogPlaylistEditVM extends DialogFragmentBaseVM<DialogPlaylistEdit, DialogPlaylistEditBinding> {

    Alarm alarm;

    /***
     * @param fragment
     * @param binding
     */
    public DialogPlaylistEditVM(DialogPlaylistEdit fragment, DialogPlaylistEditBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {
        alarm = Parcels.unwrap(fragment.getArguments().getParcelable(EXTRA.ALARM));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZikoUtils.showKeyboard(binding.etName);
    }

    public void onSave(View view) {
        AlarmManager.editAlarmName(binding.etName.getText().toString(), alarm).subscribe(alarm1 -> {
            fragment.dismiss();
        });
    }

    @Bindable
    public String getAlarmName() {
        return alarm.getName();
    }

    @Override
    public void destroy() {

    }
}
