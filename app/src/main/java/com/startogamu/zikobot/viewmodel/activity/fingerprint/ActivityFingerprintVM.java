package com.startogamu.zikobot.viewmodel.activity.fingerprint;

import android.view.View;

import com.gracenote.gnsdk.GnException;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.databinding.ActivityFingerprintBinding;
import com.startogamu.zikobot.module.gracenote.GracenoteManager;
import com.startogamu.zikobot.view.activity.fingerprint.ActivityFingerprint;

/**
 * Created by josh on 22/07/16.
 */
public class ActivityFingerprintVM extends ActivityBaseVM<ActivityFingerprint, ActivityFingerprintBinding> {

    /***
     * @param activity
     * @param binding
     */
    public ActivityFingerprintVM(ActivityFingerprint activity, ActivityFingerprintBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        try {
            GracenoteManager.init(activity);
        } catch (GnException e) {
            e.printStackTrace();
        }
    }


    public void startIdentification(View view) {
        try {
            GracenoteManager.identify();
        } catch (GnException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }
}
