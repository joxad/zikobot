package com.startogamu.zikobot.core.event.dialog;

import com.startogamu.zikobot.core.module.localmusic.model.LocalAlbum;

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
