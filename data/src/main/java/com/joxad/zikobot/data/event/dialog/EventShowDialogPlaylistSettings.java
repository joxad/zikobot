package com.joxad.zikobot.data.event.dialog;

import com.joxad.zikobot.data.module.spotify_api.model.Item;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 20/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class EventShowDialogPlaylistSettings {
    protected final Item model;
}
