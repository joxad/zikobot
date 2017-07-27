package com.joxad.zikobot.app.playlist;

import android.content.Context;
import android.databinding.ObservableArrayList;

import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.data.db.model.ZikoPlaylist;

/**
 * Created by linux on 7/27/17.
 */

public class PlaylistVM extends BaseVM<ZikoPlaylist> {


    public ObservableArrayList<TrackVM> tracksVms;

    /***

     * @param context
     * @param model
     */
    public PlaylistVM(Context context, ZikoPlaylist model) {
        super(context, model);
    }

    @Override
    public void onCreate() {
        tracksVms = new ObservableArrayList<>();
    }

    public boolean hasTracks() {
        return tracksVms.size() > 0;
    }

}
