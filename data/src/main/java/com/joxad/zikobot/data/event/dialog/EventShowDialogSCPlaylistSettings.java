package com.joxad.zikobot.data.event.dialog;

import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 20/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventShowDialogSCPlaylistSettings {
    protected final SoundCloudPlaylist model;
}
