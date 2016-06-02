package com.startogamu.musicalarm.module.alarm.manager;

import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;
import com.startogamu.musicalarm.module.alarm.object.LocalTrack;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyPlaylistItem;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyPlaylistWithTrack;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyTrack;

import java.util.ArrayList;

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
    public static void selectTrack(SpotifyTrack track) {
        AlarmTrack alarmTrack = new AlarmTrack();
        alarmTrack.setType(AlarmTrack.TYPE.SPOTIFY);
        alarmTrack.setRef("spotify:track:" + track.getId());
        alarmTrack.setImageUrl(track.getAlbum().getImages().get(0).url);
        alarmTrack.setArtistName(track.getArtists().get(0).getName());
        alarmTrack.setName(track.getName());

        alarmTracks.add(alarmTrack);
    }


    /***
     * We create an alarm track using the localtrack model
     *
     * @param track
     */
    public static void removeTrack(SpotifyTrack track) {
        AlarmTrack trackToRemove = null;
        for (AlarmTrack alarmTrack : alarmTracks) {
            if (alarmTrack.getRef().equals(track.getId())) {
                trackToRemove = alarmTrack;
                break;
            }
        }
        if (trackToRemove != null)
            alarmTracks.remove(trackToRemove);
    }

    /***
     * We create an alarm track using the localtrack model
     *
     * @param track
     */
    public static void selectTrack(LocalTrack track) {
        AlarmTrack alarmTrack = new AlarmTrack();
        alarmTrack.setType(AlarmTrack.TYPE.LOCAL);
        alarmTrack.setRef(track.getData());
        alarmTrack.setImageUrl(track.getArtPath());
        alarmTrack.setArtistName(track.getArtist());
        alarmTrack.setName(track.getTitle());

        alarmTracks.add(alarmTrack);
    }

    /***
     * We remove an alarm track using the localtrack model
     *
     * @param track
     */
    public static void removeTrack(LocalTrack track) {
        AlarmTrack trackToRemove = null;
        for (AlarmTrack alarmTrack : alarmTracks) {
            if (alarmTrack.getRef().equals(track.getData())) {
                trackToRemove = alarmTrack;
                break;
            }
        }
        if (trackToRemove != null)
            alarmTracks.remove(trackToRemove);
    }

    /***
     * Select all the playlist
     *
     * @return
     */
    public static ArrayList<AlarmTrack> selecteAllTracks(SpotifyPlaylistWithTrack spotifyPlaylist) {
        for (SpotifyPlaylistItem item : spotifyPlaylist.getItems()) {
            selectTrack(item.getTrack());
        }
        return alarmTracks;
    }

    public static void init(Alarm alarm) {
        alarmTracks.clear();
        ;
        alarmTracks.addAll(alarm.getTracks());
    }
}
