package com.startogamu.musicalarm.job;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;

/**
 * Created by josh on 28/03/16.
 */
public class AlarmJob extends Job {
    public final static String TAG = "AlarmJob";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {

        return Result.SUCCESS;
    }
}
