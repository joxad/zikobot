package com.joxad.zikobot.app.wakeup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.ActivityWakeUpBinding;

/**
 * Created by josh on 31/05/16.
 */
public class ActivityWakeUp extends ActivityBase<ActivityWakeUpBinding, ActivityWakeUpVM> {

    @Override
    public int data() {
        return BR.activityWakeUpVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_wake_up;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        super.onCreate(savedInstanceState);
    }

    @Override
    public ActivityWakeUpVM baseActivityVM(ActivityWakeUpBinding binding, Bundle savedInstanceState) {
        return new ActivityWakeUpVM(this, binding);
    }


}
