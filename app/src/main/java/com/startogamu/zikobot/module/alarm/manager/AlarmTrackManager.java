package com.startogamu.zikobot.module.alarm.manager;

import com.startogamu.zikobot.module.alarm.model.Alarm;
import com.startogamu.zikobot.module.alarm.model.AlarmTrack;

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
