package com.startogamu.zikobot.playlist;

import android.os.Bundle;

import com.joxad.easydatabinding.fragment.DialogFragmentBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.DialogPlaylistEditBinding;
import com.startogamu.zikobot.module.zikobot.model.Alarm;

import org.parceler.Parcels;

/**
 * Created by josh on 16/08/16.
 */
public class DialogPlaylistEdit extends DialogFragmentBase<DialogPlaylistEditBinding, DialogPlaylistEditVM> {
    public static final String TAG = DialogPlaylistEdit.class.getSimpleName();

    public static DialogPlaylistEdit newInstance(Alarm alarm) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA.ALARM, Parcels.wrap(alarm));
        DialogPlaylistEdit fragment = new DialogPlaylistEdit();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int data() {
        return BR.dialogPlaylistEditVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.dialog_playlist_edit;
    }

    @Override
    public DialogPlaylistEditVM baseFragmentVM(DialogPlaylistEditBinding binding) {
        return new DialogPlaylistEditVM(this, binding);
    }
}
