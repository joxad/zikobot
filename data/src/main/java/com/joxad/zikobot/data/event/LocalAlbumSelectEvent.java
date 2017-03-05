package com.joxad.zikobot.data.event;

import android.view.View;

import com.joxad.zikobot.data.model.Album;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 06/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class LocalAlbumSelectEvent {
    private final Album model;
    private final View view;
}
