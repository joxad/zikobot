package com.startogamu.zikobot.module.zikobot.manager;

import com.startogamu.zikobot.module.zikobot.model.Alarm;
import com.startogamu.zikobot.module.zikobot.model.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 02/06/16.
 */
public class AlarmTrackManager {

    private static ArrayList<Track> tracks = new ArrayList<>();

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


    public static void init(Alarm alarm) {
        tracks.clear();
        tracks.addAll(alarm.getTracks());
    }

    public static List<Track> tracks() {
        return tracks;
    }
}
