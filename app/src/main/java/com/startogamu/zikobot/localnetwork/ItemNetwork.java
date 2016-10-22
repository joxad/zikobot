package com.startogamu.zikobot.localnetwork;

import android.net.Uri;

import org.videolan.libvlc.Media;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 29/08/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ItemNetwork {

    private final String title;
    private final Media media;
}
