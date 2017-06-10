package com.joxad.zikobot.app.youtube;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.data.module.youtube.VideoItem;

import org.greenrobot.eventbus.EventBus;

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

    public void download(View view) {
        YoutubeDownloader.INSTANCE.init(view.getContext());
        YoutubeDownloader.INSTANCE.download(model.getId(), getName());
    }

    public void onClick(View view) {
        YoutubeDownloader.INSTANCE.init(view.getContext());
        EventBus.getDefault().post(new EventSelectItemYt(model));
        YoutubeDownloader.INSTANCE.getStream(model.getId(), ytFile -> {
            model.setRef(ytFile.getUrl());
            EventBus.getDefault().post(new EventSelectItemYtLoaded(model));
        });

    }


}
