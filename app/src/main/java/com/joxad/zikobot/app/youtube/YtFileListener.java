package com.joxad.zikobot.app.youtube;

import at.huber.youtubeExtractor.YtFile;

/**
 * Created by Jocelyn on 10/06/2017.
 */

interface YtFileListener {
    public void onReceived(YtFile file);
}
