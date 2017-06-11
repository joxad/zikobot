package com.joxad.zikobot.app.youtube.download;

import com.joxad.zikobot.data.module.youtube.VideoItem;

/**
 * Created by Jocelyn on 11/06/2017.
 */

public class EventSelectItemYtDownload {
    VideoItem videoItem;
    public EventSelectItemYtDownload(VideoItem model) {
        this.videoItem = model;
    }

    public VideoItem getVideoItem() {
        return videoItem;
    }
}
