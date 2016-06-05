package com.startogamu.zikobot.module.mock;

import android.content.Context;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.module.alarm.model.AlarmTrack;
import com.startogamu.zikobot.module.spotify_api.model.Item;
import com.startogamu.zikobot.module.spotify_api.model.Tracks;
import com.startogamu.zikobot.viewmodel.base.TrackVM;
import com.startogamu.zikobot.viewmodel.items.ItemPlaylistViewModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by josh on 03/06/16.
 */
public class Mock {

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

    public static Collection<? extends TrackVM> tracks(Context context, int number) {

        ArrayList<TrackVM> items = new ArrayList<>();
        while (items.size() < number) {
            items.add(new TrackVM(context, track(context)));
        }
        return items;
    }

    public static AlarmTrack track(Context context) {
        AlarmTrack alarmTrack = new AlarmTrack();
        alarmTrack.setName(context.getString(R.string.loading));
        return alarmTrack;
    }
}
