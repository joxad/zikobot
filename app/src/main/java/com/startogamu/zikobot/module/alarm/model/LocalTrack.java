package com.startogamu.zikobot.module.alarm.model;

import android.content.ContentUris;
import android.net.Uri;

import lombok.Data;

@Data
public class LocalTrack {
    final long id;
    final String artist;
    final String title;
    final long album;
    final long duration;
    final String data;
    String artPath;

    public Uri getURI() {
        return ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
    }


}