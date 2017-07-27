package com.joxad.zikobot.app.artist;

import android.content.Context;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.data.event.EventShowArtistDetail;
import com.joxad.zikobot.data.db.model.Artist;
import com.joxad.zikobot.data.module.lastfm.discogs.LastFmManager;

import org.greenrobot.eventbus.EventBus;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by josh on 06/06/16.
 */
public class ArtistVM extends BaseVM<Artist> {

    public String imageUrl;

    /***
     * @param context
     * @param model
     */
    public ArtistVM(Context context, Artist model) {
        super(context, model);
    }


    public void onClick(View view) {

        EventBus.getDefault().post(new EventShowArtistDetail(model, view.findViewById(R.id.iv_artist)));
    }

    @Bindable
    public String getName() {
        return model.getName();
    }


    @Bindable
    public String getNbAlbums() {
        return "";//String.format("%d %s", model.getNbAlbums(), context.getString(R.string.album));
    }

    @Bindable
    public String getImageUrl() {
        if (imageUrl == null)
            return model.getImage();
        return imageUrl;
    }

    @Bindable
    public String getTransition() {
        return context.getString(R.string.transition) + model.getId();
    }

    @Override
    public void onCreate() {
        imageUrl = null;
        LastFmManager.INSTANCE.findArtist(getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(discogsResult -> {
                    imageUrl = discogsResult.getImage().get(2).getText();
                    notifyPropertyChanged(BR.imageUrl);
                }, throwable -> {
                    Log.e("er", throwable.getLocalizedMessage());
                });
    }
}
