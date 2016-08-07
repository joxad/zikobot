package com.startogamu.zikobot.viewmodel.fragment.search;

import com.startogamu.zikobot.module.zikobot.model.Track;

import lombok.Data;

/**
 * Created by josh on 06/08/16.
 */
@Data
public class SearchResult {
    public final static int ARTIST = 0;
    public final static int ALBUM = 1;
    public final static int TRACK = 2;
    public final static int HEADER = 3;

    private final int type;
    private final String title;
    private final int href;
}
