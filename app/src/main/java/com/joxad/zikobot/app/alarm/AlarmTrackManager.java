package com.joxad.zikobot.app.alarm;

import com.orhanobut.logger.Logger;
import com.joxad.zikobot.app.core.model.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 02/06/16.
 */
public class AlarmTrackManager {

    private static ArrayList<Track> tracks = new ArrayList<>();
    private static String TAG = AlarmTrackManager.class.getSimpleName();

    /***
     * Selection of a track
     *
     * @param track
     */
    public static void selectTrack(Track track) {
        tracks.add(track);
    }


    /***
     * Remove of a track
     *
     * @param track
     */
    public static void removeTrack(Track track) {
        tracks.remove(track);
    }

    public static List<Track> tracks() {
        return tracks;
    }

    public static void clear() {
        tracks.clear();
    }

}
