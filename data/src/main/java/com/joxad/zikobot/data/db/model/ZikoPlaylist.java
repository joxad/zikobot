package com.joxad.zikobot.data.db.model;

import com.joxad.zikobot.data.db.MusicAlarmDatabase;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudTrack;
import com.joxad.zikobot.data.module.spotify_api.model.Item;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyTrack;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 19/06/16.
 */
@Table(database = MusicAlarmDatabase.class)
public class ZikoPlaylist extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Getter
    @Setter
    protected long id;
    @Column
    protected String name;
    @Column
    protected int type;
    @Column
    protected int nbTracks;
    @Column
    protected String imageUrl;
    @Column
    protected String spotifyId;
    @Column
    protected int soundcloudId;
    @ForeignKey(stubbedRelationship = true)
    ZikoAlarm zikoAlarm;

    protected List<Track> tracks;

    public ZikoPlaylist() {
    }

    public ZikoPlaylist(String name, int type, int nbTracks, String imageUrl, ArrayList<Track> tracks) {
        this.name = name;
        this.type = type;
        this.nbTracks = nbTracks;
        this.imageUrl = imageUrl;
        this.tracks = tracks;
    }


    /****
     * @return
     */
    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE}, variableName = "tracks")
    public List<Track> getTracks() {
        tracks = SQLite.select()
                .from(Track.class)
                .where(Track_Table.zikoPlaylistForeignKey_id.eq(id))
                .queryList();

        return tracks;
    }

    public static ZikoPlaylist fromSpotifyPlaylist(Item item) {

        ArrayList<Track> tracks = new ArrayList<>();
        for (SpotifyTrack spotifyTrack : item.getTracks().getItems()) {
            tracks.add(Track.from(spotifyTrack));
        }
        ZikoPlaylist zikoPlaylist = new ZikoPlaylist(item.getName(),TYPE.SPOTIFY,item.tracks.getTotal(),item.getImages().get(0).getUrl(),tracks);
        zikoPlaylist.spotifyId = item.getId();
        return zikoPlaylist;
    }

    public static ZikoPlaylist fromSoundCloudPlaylist(SoundCloudPlaylist soundCloudPlaylist) {
        ArrayList<Track> tracks = new ArrayList<>();
        for (SoundCloudTrack soundCloudTrack : soundCloudPlaylist.getSoundCloudTracks()) {
            tracks.add(Track.from(soundCloudTrack));
        }
        ZikoPlaylist zikoPlaylist = new ZikoPlaylist(soundCloudPlaylist.getTitle(),TYPE.SPOTIFY,soundCloudPlaylist.trackCount,soundCloudPlaylist.getArtworkUrl(),tracks);
        zikoPlaylist.soundcloudId = soundCloudPlaylist.getId();
        return zikoPlaylist;
    }


}
