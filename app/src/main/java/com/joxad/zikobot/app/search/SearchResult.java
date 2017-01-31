package com.joxad.zikobot.app.search;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 06/08/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SearchResult {
    public final static int ARTIST = 0;
    public final static int ALBUM = 1;
    public final static int TRACK = 2;
    public final static int HEADER = 3;

    private final int type;
    private final String title;
    private final int href;
}
