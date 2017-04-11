package com.joxad.zikobot.app.youtube;

import android.content.Context;
import android.databinding.Bindable;
import android.net.Uri;
import android.view.View;

import com.commit451.rxyoutubeextractor.RxYouTubeExtractor;
import com.commit451.youtubeextractor.YouTubeExtractionResult;
import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.data.module.youtube.VideoItem;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        //http://www.youtubeinmp3.com/api/
        RxYouTubeExtractor rxYouTubeExtractor = RxYouTubeExtractor.create();
        Observable<YouTubeExtractionResult> result = rxYouTubeExtractor.extract(model.getId());
        result.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<YouTubeExtractionResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        //Alert your user!
                    }

                    @Override
                    public void onNext(YouTubeExtractionResult youTubeExtractionResult) {
                        Uri hdUri = youTubeExtractionResult.getDefaultThumbUri();
                        //See the sample for more
                    }
                });
    }
}
