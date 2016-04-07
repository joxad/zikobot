package com.startogamu.musicalarm.di.manager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.startogamu.musicalarm.model.LocalTrack;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Josh on 06/04/2016.
 */
@Singleton
public class LocalMusicManager {

    private static final String TAG = LocalMusicManager.class.getSimpleName();
    ContentResolver contentResolver;

    @Inject
    public LocalMusicManager(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public Observable<List<LocalTrack>> getLocalTracks() {

        return Observable.create(new Observable.OnSubscribe<List<LocalTrack>>() {
            @Override
            public void call(Subscriber<? super List<LocalTrack>> subscriber) {
                Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                // Perform a query on the content resolver. The URI we're passing specifies that we
                // want to query for all audio media on external storage (e.g. SD card)
                Cursor cur = contentResolver.query(uri, null,
                        MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
                if (cur == null) {
                    Log.e(TAG, "Failed to retrieve music: cursor is null :-(");
                    return;
                }
                if (!cur.moveToFirst()) {
                    Log.e(TAG, "Failed to move cursor to first row (no query results).");
                    return;
                }
                int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
                int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
                int dataColumn = cur.getColumnIndex(MediaStore.Audio.Media.DATA);
                ArrayList<LocalTrack> tracks = new ArrayList<>();

                do {
                    Log.i(TAG, "ID: " + cur.getString(idColumn) + " Title: " + cur.getString(titleColumn));
                    tracks.add(new LocalTrack(
                            cur.getLong(idColumn),
                            cur.getString(artistColumn),
                            cur.getString(titleColumn),
                            cur.getLong(albumColumn),
                            cur.getLong(durationColumn),
                            cur.getString(dataColumn)));
                } while (cur.moveToNext());
                Log.i(TAG, "Done querying media. MusicRetriever is ready.");
                cur.close();


                for (LocalTrack localTrack : tracks) {
                    localTrack.setArtPath(findAlbumArt(localTrack));
                }
                subscriber.onNext(tracks);
            }
        });

    }

    public String findAlbumArt(LocalTrack localTrack) {
        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{String.valueOf(localTrack.getAlbum())},
                null);

        String path = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                // do whatever you need to do
            }
        }
        return path;
    }
}
