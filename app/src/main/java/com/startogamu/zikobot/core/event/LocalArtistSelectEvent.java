package com.startogamu.zikobot.core.event;

import com.startogamu.zikobot.module.content_resolver.model.LocalArtist;

import lombok.Data;

/**
 * Created by josh on 06/06/16.
 */
@Data
public class LocalArtistSelectEvent {
    private final LocalArtist localArtist;
}
