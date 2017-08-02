package com.joxad.zikobot.data.db.model;

import com.joxad.zikobot.data.db.ZikoDB;
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
@Table(database = ZikoDB.class)
public class ZikoPlaylist extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Getter
    @Setter
    protected long id;
    @Column
    protected String name;
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

    protected List<ZikoTrack> tracks;

    public ZikoPlaylist() {
    }

    public ZikoPlaylist(String name, int nbTracks, String imageUrl, ArrayList<ZikoTrack> tracks) {
        this.name = name;
        this.nbTracks = nbTracks;
        this.imageUrl = imageUrl;
        this.tracks = tracks;
    }


    /****
     * @return
     */
    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE}, variableName = "tracks")
    public List<ZikoTrack> getTracks() {
        tracks = SQLite.select()
                .from(ZikoTrack.class)
                .where(ZikoTrack_Table.zikoPlaylist_id.eq(id))
                .queryList();

        return tracks;
    }

    public static ZikoPlaylist fromSpotifyPlaylist(Item item) {

        ArrayList<ZikoTrack> tracks = new ArrayList<>();
        for (SpotifyTrack spotifyTrack : item.getTracks().getItems()) {
            tracks.add(ZikoTrack.from(spotifyTrack));
        }
        ZikoPlaylist zikoPlaylist = new ZikoPlaylist(item.getName(), item.getTracks().getTotal(),item.getImages().get(0).getUrl(),tracks);
        zikoPlaylist.spotifyId = item.getId();
        return zikoPlaylist;
    }

    public static ZikoPlaylist fromSoundCloudPlaylist(SoundCloudPlaylist soundCloudPlaylist) {
        ArrayList<ZikoTrack> tracks = new ArrayList<>();
        for (SoundCloudTrack soundCloudTrack : soundCloudPlaylist.getSoundCloudTracks()) {
            tracks.add(ZikoTrack.from(soundCloudTrack));
        }
        ZikoPlaylist zikoPlaylist = new ZikoPlaylist(soundCloudPlaylist.getTitle(), soundCloudPlaylist.getTrackCount(),soundCloudPlaylist.getArtworkUrl(),tracks);
        zikoPlaylist.soundcloudId = soundCloudPlaylist.getId();
        return zikoPlaylist;
    }


}
