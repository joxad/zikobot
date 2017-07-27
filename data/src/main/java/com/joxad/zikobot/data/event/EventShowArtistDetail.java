package com.joxad.zikobot.data.event;

import android.view.View;

import com.joxad.zikobot.data.db.model.Artist;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 06/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventShowArtistDetail {
    private final Artist artist;
    private final View view;
}
