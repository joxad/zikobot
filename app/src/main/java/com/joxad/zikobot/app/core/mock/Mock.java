package com.joxad.zikobot.app.core.mock;

import android.content.Context;

import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.soundcloud.SoundCloudPlaylistVM;
import com.joxad.zikobot.app.spotify.ItemPlaylistViewModel;

import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.joxad.zikobot.data.module.spotify_api.model.Item;
import com.joxad.zikobot.data.module.spotify_api.model.Tracks;
import com.joxad.zikobot.data.model.Track;

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


    public static ArrayList<SoundCloudPlaylistVM> scPlaylists(Context context) {
        ArrayList<SoundCloudPlaylistVM> items = new ArrayList<>();
        while (items.size() < 10) {
            items.add(new SoundCloudPlaylistVM(context, scPlaylist(context)));
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
        track.setName(context.getString(R.string.tuto_select_track));
        track.setArtistName("");
        return track;
    }


}
