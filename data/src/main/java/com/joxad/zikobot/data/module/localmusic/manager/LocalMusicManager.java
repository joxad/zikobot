package com.joxad.zikobot.data.module.localmusic.manager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.joxad.zikobot.data.module.localmusic.model.LocalAlbum;
import com.joxad.zikobot.data.module.localmusic.model.LocalArtist;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;
import com.joxad.zikobot.data.module.youtube.VideoItem;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Josh on 06/04/2016.
 */

public class LocalMusicManager {

    public static final String QUERY_PARAMETER_LIMIT = "limit";
    public static final String QUERY_PARAMETER_OFFSET = "offset";

    private static final String TAG = LocalMusicManager.class.getSimpleName();
    ContentResolver contentResolver;

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static LocalMusicManager getInstance() {
        return LocalMusicManager.LocalMusicManagerHolder.instance;
    }

    public void init(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    private Uri contentUri(Uri externalContentUri, final int limit, final int offset) {
        return externalContentUri.buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_LIMIT,
                        String.valueOf(limit))
                .appendQueryParameter(QUERY_PARAMETER_OFFSET,
                        String.valueOf(offset))
                .build();
    }

    /***
     * Give a list of tracks according to an album
     *
     * @param album -1 if you want all the tracks
     * @return
     */
    public Observable<List<LocalTrack>> getLocalTracks(final int limit, final int offset, @Nullable final String artist, @Nullable final long album, @Nullable final String query) {
        return Observable.create(subscriber -> {
            String[] projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.IS_MUSIC,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.TRACK,
                    MediaStore.Audio.Media.DATA
            };
            String selection = MediaStore.Audio.Media.IS_MUSIC + " = 1";
            String[] selectionArgs = null;
            if (artist != null) {
                selection += " AND " + MediaStore.Audio.Media.ARTIST + "='" + artist + "'";
            }
            if (album != -1) {
                selection += " AND " + MediaStore.Audio.Media.ALBUM_ID + "='" + album + "'";
            }
            if (query != null) {
                selection += " AND " + MediaStore.Audio.Media.TITLE + " like '%" + query + "%'";
            }
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC limit " + limit + " offset " + offset;
            Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);
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
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            ArrayList<LocalTrack> tracks = new ArrayList<>();
            do {
                Log.i(TAG, "ID: " + cursor.getString(idColumn) + " Title: " + cursor.getString(titleColumn));
                tracks.add(new LocalTrack(
                        cursor.getLong(idColumn),
                        cursor.getString(artistColumn),
                        cursor.getString(titleColumn),
                        cursor.getLong(albumColumn),
                        cursor.getLong(durationColumn),
                        cursor.getString(dataColumn)));
            } while (cursor.moveToNext());
            Log.i(TAG, "Done querying media. MusicRetriever is ready.");
            cursor.close();
            for (LocalTrack localTrack : tracks) {
                localTrack.setArtPath(findAlbumArt(localTrack.getAlbum()));
            }
            subscriber.onNext(tracks);
        });
    }

    /***
     * @return
     */
    public Observable<List<LocalArtist>> getLocalArtists(int limit, int offset, @Nullable final String query) {
        return Observable.create(subscriber -> {
            String[] projection = new String[]{
                    MediaStore.Audio.Artists._ID,
                    MediaStore.Audio.Artists.ARTIST,
                    MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                    MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
            String selection = null;
            if (query != null) {
                selection = "" + MediaStore.Audio.Artists.ARTIST + " like '%" + query + "%'";
            }
            String[] selectionArgs = null;
            String sortOrder = MediaStore.Audio.Media.ARTIST + " ASC limit " + limit + " offset " + offset;
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
                artists.add(new LocalArtist(cursor.getLong(0), cursor.getString(1), cursor.getInt(2)));
            } while (cursor.moveToNext());
            cursor.close();
            Log.i(TAG, "Done querying media. MusicRetriever is ready.");
            for (LocalArtist localArtist : artists) {
                localArtist.setImage(findAlbumArtByArtist(localArtist.getName()));
            }
            subscriber.onNext(artists);
        });

    }

    /***
     * @param artist null to get all
     * @return
     */
    public Observable<List<LocalAlbum>> getLocalAlbums(int limit, int offset, @Nullable final String artist, @Nullable final String query) {

        return Observable.create(subscriber -> {

            String[] projection = new String[]{
                    MediaStore.Audio.Albums._ID,
                    MediaStore.Audio.Albums.ALBUM,
                    MediaStore.Audio.Albums.ARTIST,
                    MediaStore.Audio.Albums.ALBUM_ART,
                    MediaStore.Audio.Albums.NUMBER_OF_SONGS};
            String selection = null;
            String[] selectionArgs = null;
            if (artist != null) {
                selection = MediaStore.Audio.Albums.ARTIST + "='" + artist + "'";
            }
            if (query != null) {
                selection = MediaStore.Audio.Albums.ARTIST + " like '%" + query + "%'";
                selection += " OR " + MediaStore.Audio.Albums.ALBUM + " like '%" + query + "%'";
            }
            String sortOrder = MediaStore.Audio.Media.ALBUM + " ASC limit " + limit + " offset " + offset;
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
            int albumId = cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int artColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int nbTrackColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
            ArrayList<LocalAlbum> albums = new ArrayList<>();

            do {
                Log.i(TAG, "ID: " + cursor.getString(0) + " Title: " + cursor.getString(1));
                albums.add(new LocalAlbum(cursor.getLong(albumId), cursor.getString(albumColumn), cursor.getString(artistColumn),
                        cursor.getString(artColumn), cursor.getInt(nbTrackColumn)));
            } while (cursor.moveToNext());
            Log.i(TAG, "Done querying media. MusicRetriever is ready.");

            cursor.close();

            subscriber.onNext(albums);
        });

    }

    /***
     * @param albumId
     * @return
     */
    public String findAlbumArt(long albumId) {
        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{String.valueOf(albumId)},
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

    /***
     * @param artistName
     * @return
     */
    public String findAlbumArtByArtist(String artistName) {
        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums.ARTIST + "=?",
                new String[]{artistName},
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

    public void update(String folder, VideoItem videoItem, String artist, String album) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.ARTIST, artist);
        values.put(MediaStore.Audio.Media.ALBUM, album);
        values.put(MediaStore.Audio.Media.DATA, folder + "/" + videoItem.getTitle() + ".mp3");
        values.put(MediaStore.Audio.Media.IS_MUSIC, true);
        values.put(MediaStore.Audio.Media.DURATION, videoItem.getDuration());
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.TITLE, videoItem.getTitle());
        contentResolver.insert(MediaStore.Audio.Media.getContentUriForPath(folder + "/" + videoItem.getTitle() + ".mp3"), values);

        ContentValues imageValues = new ContentValues();
        imageValues.put(MediaStore.Audio.Albums.ARTIST, artist);
        imageValues.put(MediaStore.Audio.Albums.ALBUM_ART, videoItem.getThumbnailURL());
        
        contentResolver.insert(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, imageValues);
    }

    /***
     * @param albumName
     * @return
     */
//    public void editAlbumName(Uri albumUri,String albumName) {
//        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
//                new String[]{MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.ALBUM_ART},
//                MediaStore.Audio.Albums.ARTIST + "=?",
//                new String[]{albumName},
//                null);
//        ContentValues newLineValues = new ContentValues();
//        newLineValues.put(MediaStore.Audio.Albums.ALBUM);
////Pour mettre à jour une ligne connue
//        contentResolver.update(albumUri, newLineValues, null, null);
////Pour mettre à jour un sous-ensemble :
//        return path;
//    }

    /**
     * Holder
     */
    private static class LocalMusicManagerHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static LocalMusicManager instance = new LocalMusicManager();
    }

}
