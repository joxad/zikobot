package com.startogamu.zikobot.viewmodel.fragment.search;

import android.content.Context;

import com.joxad.easydatabinding.base.BaseVM;

/**
 * Created by josh on 06/08/16.
 */
public class SearchResultVM extends BaseVM<SearchResult> {
    /***
     * @param context
     * @param model
     */
    public SearchResultVM(Context context, SearchResult model) {
        super(context, model);
    }



    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    public boolean isTrack() {
        return model.getType() == SearchResult.TRACK;
    }


    public boolean isAlbum() {
        return model.getType() == SearchResult.ALBUM;
    }


    public boolean isArtist() {
        return model.getType() == SearchResult.ARTIST;
    }
}
