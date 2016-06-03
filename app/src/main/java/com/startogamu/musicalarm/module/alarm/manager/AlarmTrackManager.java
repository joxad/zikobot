package com.startogamu.musicalarm.module.alarm.manager;

import com.startogamu.musicalarm.module.alarm.model.Alarm;
import com.startogamu.musicalarm.module.alarm.model.AlarmTrack;
import com.startogamu.musicalarm.module.alarm.model.LocalTrack;
import com.startogamu.musicalarm.module.spotify_api.model.SpotifyPlaylistItem;
import com.startogamu.musicalarm.module.spotify_api.model.SpotifyPlaylistWithTrack;
import com.startogamu.musicalarm.module.spotify_api.model.SpotifyTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 02/06/16.
 */
public class AlarmTrackManager {

    private static ArrayList<AlarmTrack> alarmTracks = new ArrayList<>();

    /***
     * Selection of a track
     *
     * @param track
     */
    public static void selectTrack(AlarmTrack track) {
        alarmTracks.add(track);
    }



    /***
     * Remove of a track
     *
     * @param track
     */
    public static void removeTrack(AlarmTrack track) {
        alarmTracks.remove(track);
    }


    public static void init(Alarm alarm) {
        alarmTracks.clear();
        alarmTracks.addAll(alarm.getTracks());
    }

    public static List<AlarmTrack> tracks() {
        return alarmTracks;
    }
}
