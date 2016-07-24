package com.startogamu.zikobot.view.activity.fingerprint;

import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivityFingerprintBinding;
import com.startogamu.zikobot.viewmodel.activity.fingerprint.ActivityFingerprintVM;

/**
 * Created by josh on 22/07/16.
 */
public class ActivityFingerprint extends ActivityBase<ActivityFingerprintBinding,ActivityFingerprintVM> {


    @Override
    public int data() {
        return BR.fragmentFingerprintVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_fingerprint;
    }

    @Override
    public ActivityFingerprintVM baseActivityVM(ActivityFingerprintBinding binding, Bundle savedInstanceState) {
        return new ActivityFingerprintVM(this,binding);
    }


}
