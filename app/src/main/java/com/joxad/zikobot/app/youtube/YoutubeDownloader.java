package com.joxad.zikobot.app.youtube;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.notification.DownloadNotification;
import com.joxad.zikobot.app.youtube.download.EventDownloadDone;
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.data.module.youtube.VideoItem;
import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.request.Request;

import org.greenrobot.eventbus.EventBus;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

/**
 * Created by Jocelyn on 10/06/2017.
 */

public enum YoutubeDownloader {
    INSTANCE;
    Fetch fetch;
    Context context;

    public void init(Context context) {
        this.context = context;
        fetch = Fetch.newInstance(context);
    }

    public void download(VideoItem model, String artist, String album) {
        String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                .toString() + "/zikobot";
        Fetch fetch = Fetch.newInstance(context);
        fetch.enableLogging(true);
        String youtubeLink = "http://youtube.com/watch?v=" + model.getId();
        Log.d("Download", folder);
        new YouTubeExtractor(context) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                    return;
                }
                YtFile ytFile = getBestStream(ytFiles);
                Request request = new Request(ytFile.getUrl(), folder, model.getTitle() + ".mp3");
                long downloadId = fetch.enqueue(request);

                if (downloadId != Fetch.ENQUEUE_ERROR_ID) {
                    //Download was successfully queued for download.
                    DownloadNotification downloadNotification = new DownloadNotification(context, model.getTitle(), downloadId);
                    fetch.addFetchListener((id, status, progress, downloadedBytes, fileSize, error) -> {
                        downloadNotification.updateProgress(progress);
                        if (progress == 100) {

                            downloadNotification.cancel();
                            EventBus.getDefault().post(new EventDownloadDone());
                            LocalMusicManager.getInstance().update(folder, model, artist, album == null ? artist : album);
                        }
                    });
                }
            }
        }.extract(youtubeLink, true, true);
    }

    public void getStream(String trackId, YtFileListener ytFileYtFileListener) {
        String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                .toString() + "/zikobot";
        Fetch fetch = Fetch.newInstance(context);
        fetch.enableLogging(true);
        String youtubeLink = "http://youtube.com/watch?v=" + trackId;
        Log.d("Download", folder);
        new YouTubeExtractor(context) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    Toast.makeText(context, R.string.download_fail, Toast.LENGTH_SHORT).show();
                    ytFileYtFileListener.onReceived(null);

                    return;
                }
                YtFile ytFile = getBestStream(ytFiles);
                ytFileYtFileListener.onReceived(ytFile);
            }
        }.extract(youtubeLink, true, true);
    }


    /**
     * Get the best available audio stream
     *
     * @param ytFiles Array of available streams
     * @return Audio stream with highest bitrate
     */
    private YtFile getBestStream(SparseArray<YtFile> ytFiles) {
        if (ytFiles.get(141) != null) {
            return ytFiles.get(141); //mp4a - stereo, 44.1 KHz 256 Kbps
        } else if (ytFiles.get(251) != null) {
            return ytFiles.get(251); //webm - stereo, 48 KHz 160 Kbps
        } else if (ytFiles.get(140) != null) {
            return ytFiles.get(140);  //mp4a - stereo, 44.1 KHz 128 Kbps
        }
        return ytFiles.get(17); //mp4 - stereo, 44.1 KHz 96-100 Kbps
    }
}
