package com.startogamu.zikobot.module.mock;

import android.content.Context;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.module.spotify_api.model.Item;
import com.startogamu.zikobot.module.spotify_api.model.Tracks;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.viewmodel.base.TrackVM;
import com.startogamu.zikobot.viewmodel.items.ItemPlaylistViewModel;
import com.startogamu.zikobot.viewmodel.items.ItemSCPlaylistViewModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by josh on 03/06/16.
 */
public class Mock {

    public static SoundCloudPlaylist scPlaylist(Context context) {
        SoundCloudPlaylist soundCloudPlaylist = new SoundCloudPlaylist();
        soundCloudPlaylist.setTitle(context.getString(R.string.loading));
        return soundCloudPlaylist;
    }

    public static Item playlist(Context context) {
        Item spotifyPlaylist = new Item();
        spotifyPlaylist.setName(context.getString(R.string.loading));
        spotifyPlaylist.tracks = new Tracks();
        return spotifyPlaylist;
    }

    public static ArrayList<ItemPlaylistViewModel> playlists(Context context) {

        ArrayList<ItemPlaylistViewModel> items = new ArrayList<>();
        while (items.size() < 10) {
            items.add(new ItemPlaylistViewModel(context, playlist(context)));
        }
        return items;
    }


    public static ArrayList<ItemSCPlaylistViewModel> scPlaylists(Context context) {
        ArrayList<ItemSCPlaylistViewModel> items = new ArrayList<>();
        while (items.size() < 10) {
            items.add(new ItemSCPlaylistViewModel(context, scPlaylist(context)));
        }
        return items;
    }

    public static Collection<? extends TrackVM> tracks(Context context, int number) {

        ArrayList<TrackVM> items = new ArrayList<>();
        while (items.size() < number) {
            items.add(new TrackVM(context, track(context)));
        }
        return items;
    }

    /**
     * @param context
     * @return
     */
    public static Track track(Context context) {
        Track track = new Track();
        track.setName(context.getString(R.string.loading));
        return track;
    }

    public static Track trackPlayer(Context context) {
        Track track = new Track();
        track.setName(context.getString(R.string.activity_alarm_select_song));
        track.setArtistName("");
        return track;
    }
}
