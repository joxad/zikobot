package com.startogamu.musicalarm.job;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class AlarmJobCreator implements JobCreator {

    @Override
    public Job create(final String tag) {
        switch (tag) {
            case AlarmJob.TAG:
                return new AlarmJob();
            default:
                return null;
        }
    }
}