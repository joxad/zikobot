package com.startogamu.zikobot.module.content_resolver.manager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalArtist;
import com.startogamu.zikobot.module.content_resolver.model.LocalTrack;

import java.util.ArrayList;
import java.util.List;

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

    public LocalMusicManager(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }


    /***
     * @return
     */
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
                    subscriber.onError(new Throwable("Failed to retrieve music: cursor is null :-("));
                    return;
                }
                if (!cur.moveToFirst()) {
                    subscriber.onError(new Throwable("No results. :( Add some tracks!"));

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


    /***
     * @return
     */
    public Observable<List<LocalArtist>> getLocalArtists() {

        return Observable.create(new Observable.OnSubscribe<List<LocalArtist>>() {
            @Override
            public void call(Subscriber<? super List<LocalArtist>> subscriber) {

                String[] projection = new String[]{
                        MediaStore.Audio.Artists._ID,
                        MediaStore.Audio.Artists.ARTIST,
                        MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                        MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                        MediaStore.Audio.Artists.ARTIST_KEY};
                String selection = null;
                String[] selectionArgs = null;
                String sortOrder = MediaStore.Audio.Media.ARTIST + " ASC";
                Cursor cursor = contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);

                if (cursor == null) {
                    Log.e(TAG, "Failed to retrieve music: cursor is null :-(");
                    subscriber.onError(new Throwable("Failed to retrieve music: cursor is null :-("));
                    return;
                }
                if (!cursor.moveToFirst()) {
                    subscriber.onError(new Throwable("No results. :( Add some tracks!"));

                    Log.e(TAG, "Failed to move cursor to first row (no query results).");
                    return;
                }

                ArrayList<LocalArtist> artists = new ArrayList<>();

                do {
                    Log.i(TAG, "ID: " + cursor.getString(0) + " Title: " + cursor.getString(1));
                    artists.add(new LocalArtist(cursor.getLong(0),cursor.getString(1), cursor.getInt(2)));
                } while (cursor.moveToNext());
                Log.i(TAG, "Done querying media. MusicRetriever is ready.");
                subscriber.onNext(artists);
                cursor.close();
            }
        });

    }

    /***
     *
     * @return
     */
    public Observable<List<LocalAlbum>> getLocalAlbums() {

        return Observable.create(new Observable.OnSubscribe<List<LocalAlbum>>() {
            @Override
            public void call(Subscriber<? super List<LocalAlbum>> subscriber) {

                String[] projection = new String[]{
                        MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ALBUM,
                        MediaStore.Audio.Albums.ARTIST,
                        MediaStore.Audio.Albums.ALBUM_ART,
                        MediaStore.Audio.Albums.NUMBER_OF_SONGS};
                String selection = null;
                String[] selectionArgs = null;
                String sortOrder = MediaStore.Audio.Media.ALBUM + " ASC";
                Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);

                if (cursor == null) {
                    Log.e(TAG, "Failed to retrieve music: cursor is null :-(");
                    subscriber.onError(new Throwable("Failed to retrieve music: cursor is null :-("));
                    return;
                }
                if (!cursor.moveToFirst()) {
                    subscriber.onError(new Throwable("No results. :( Add some tracks!"));
                    Log.e(TAG, "Failed to move cursor to first row (no query results).");
                    return;
                }

                ArrayList<LocalAlbum> albums = new ArrayList<>();

                do {
                    Log.i(TAG, "ID: " + cursor.getString(0) + " Title: " + cursor.getString(1));
                    albums.add(new LocalAlbum(cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getInt(4)));
                } while (cursor.moveToNext());
                Log.i(TAG, "Done querying media. MusicRetriever is ready.");
                subscriber.onNext(albums);
                cursor.close();
            }
        });

    }

        /***
         * @param localTrack
         * @return
         */
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
                    cursor.close();
                }
            }

            return path;
        }



}
