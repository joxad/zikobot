package com.joxad.zikobot.data.event.dialog;

import com.joxad.zikobot.data.module.localmusic.model.LocalAlbum;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 20/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventShowDialogAlbumSettings {
    protected final LocalAlbum model;
}
