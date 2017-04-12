package com.joxad.zikobot.app.youtube;

import android.content.Context;
import android.databinding.Bindable;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.player.event.EventPlayTrack;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.youtube.VideoItem;

import org.greenrobot.eventbus.EventBus;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

/**
 * Created by Jocelyn on 10/04/2017.
 */

public class YoutubeItemVM extends BaseVM<VideoItem> {
    /***

     * @param context
     * @param model
     */
    public YoutubeItemVM(Context context, VideoItem model
    ) {
        super(context, model);
    }

    @Override
    public void onCreate() {

    }

    @Bindable
    public String getName() {
        return model.getTitle();
    }

    @Bindable
    public String getImageUrl() {
        return model.getThumbnailURL();
    }

    public void onClick(View view) {
        String youtubeLink = "http://youtube.com/watch?v=" + model.getId();
        new YouTubeExtractor(context) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                    return;
                }
                YtFile ytFile = getBestStream(ytFiles);
                model.setRef(ytFile.getUrl());
                EventBus.getDefault().post(new EventSelectItemYt(model));

            }
        }.extract(youtubeLink,true,true);

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
