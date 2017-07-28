package com.joxad.zikobot.data.module.localmusic.manager;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;

import java.util.ArrayList;

public class TrackLoader {
    protected static final String BASE_SELECTION = AudioColumns.IS_MUSIC + "=1" + " AND " + AudioColumns.TITLE + " != ''";

    @NonNull
    public static ArrayList<LocalTrack> getAllTracks(@NonNull Context context) {
        Cursor cursor = makeTrackCursor(context, null, null);
        return getTracks(cursor);
    }

    @NonNull
    public static ArrayList<LocalTrack> getTracks(@NonNull final Context context, final String query) {
        Cursor cursor = makeTrackCursor(context, AudioColumns.TITLE + " LIKE ?", new String[]{"%" + query + "%"});
        return getTracks(cursor);
    }

    @NonNull
    public static LocalTrack getTrack(@NonNull final Context context, final int queryId) {
        Cursor cursor = makeTrackCursor(context, AudioColumns._ID + "=?", new String[]{String.valueOf(queryId)});
        return getTrack(cursor);
    }

    @NonNull
    public static ArrayList<LocalTrack> getTracks(@Nullable final Cursor cursor) {
        ArrayList<LocalTrack> tracks = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                tracks.add(getTrackFromCursorImpl(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null)
            cursor.close();
        return tracks;
    }

    @NonNull
    public static LocalTrack getTrack(@Nullable Cursor cursor) {
        LocalTrack track;
        if (cursor != null && cursor.moveToFirst()) {
            track = getTrackFromCursorImpl(cursor);
        } else {
            track = LocalTrack.Companion.getEMPTY();
        }
        if (cursor != null) {
            cursor.close();
        }
        return track;
    }

    @NonNull
    private static LocalTrack getTrackFromCursorImpl(@NonNull Cursor cursor) {
        final int id = cursor.getInt(0);
        final String title = cursor.getString(1);
        final int trackNumber = cursor.getInt(2);
        final int year = cursor.getInt(3);
        final long duration = cursor.getLong(4);
        final String data = cursor.getString(5);
        final long dateModified = cursor.getLong(6);
        final int albumId = cursor.getInt(7);
        final String albumName = cursor.getString(8);
        final int artistId = cursor.getInt(9);
        final String artistName = cursor.getString(10);

        return new LocalTrack(id, title, trackNumber, year, duration, data, dateModified, albumId, albumName, artistId, artistName);
    }

    @Nullable
    public static Cursor makeTrackCursor(@NonNull final Context context, @Nullable final String selection, final String[] selectionValues) {
        return makeTrackCursor(context, selection, selectionValues,"");
    }

    @Nullable
    public static Cursor makeTrackCursor(@NonNull final Context context, @Nullable final String selection, final String[] selectionValues, final String sortOrder) {
        String baseSelection = BASE_SELECTION;
        if (selection != null && !selection.trim().equals("")) {
            baseSelection += " AND " + selection;
        }

        try {
            return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            BaseColumns._ID,// 0
                            AudioColumns.TITLE,// 1
                            AudioColumns.TRACK,// 2
                            AudioColumns.YEAR,// 3
                            AudioColumns.DURATION,// 4
                            AudioColumns.DATA,// 5
                            AudioColumns.DATE_MODIFIED,// 6
                            AudioColumns.ALBUM_ID,// 7
                            AudioColumns.ALBUM,// 8
                            AudioColumns.ARTIST_ID,// 9
                            AudioColumns.ARTIST,// 10

                    }, baseSelection, selectionValues, sortOrder);
        } catch (SecurityException e) {
            return null;
        }
    }
}