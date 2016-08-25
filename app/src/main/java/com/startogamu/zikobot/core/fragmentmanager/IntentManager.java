package com.startogamu.zikobot.core.fragmentmanager;

import android.content.Context;
import android.content.Intent;

import com.deezer.sdk.model.Playlist;
import com.startogamu.zikobot.album.ActivityAlbum;
import com.startogamu.zikobot.artist.ActivityArtist;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.deezer.ActivityDeezer;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.module.spotify_api.model.Item;
import com.startogamu.zikobot.module.zikobot.model.Alarm;
import com.startogamu.zikobot.module.zikobot.model.Album;
import com.startogamu.zikobot.module.zikobot.model.Artist;
import com.startogamu.zikobot.search.ActivitySearch;
import com.startogamu.zikobot.soundcloud.ActivitySoundCloud;
import com.startogamu.zikobot.spotify.ActivitySpotify;
import com.startogamu.zikobot.view.Henson;
import com.startogamu.zikobot.view.activity.ActivityAlarm;

import org.parceler.Parcels;

/**
 * Created by josh on 08/08/16.
 */
public class IntentManager {

    private static Context context;

    public static void init(Context c) {
        context = c;
    }

    public static Intent goToArtist(Artist artist) {
        Intent intent = new Intent(context, ActivityArtist.class);
        intent.putExtra(EXTRA.LOCAL_ARTIST, Parcels.wrap(artist));
        return intent;
    }

    public static Intent goToAlarm(Alarm alarm) {
        Intent intent = new Intent(context, ActivityAlarm.class);
        intent.putExtra(EXTRA.ALARM, Parcels.wrap(alarm));
        return intent;

    }

    public static Intent goToMainFromWidget() {
        return Henson.with(context).gotoActivityMain().fromWidget("ALARM").build();
    }

    public static Intent goToTuto() {
        return Henson.with(context).gotoActivityFirstStart().build();
    }

    public static Intent goToSettings() {
        return Henson.with(context).gotoActivitySettings().build();
    }

    public static Intent goToWakeUp(Alarm alarm) {
        return Henson.with(context).gotoActivityWakeUp().alarm(alarm).build();
    }

    public static Intent goToAlbum(Album model) {
        return new Intent(context, ActivityAlbum.class).putExtra(EXTRA.LOCAL_ALBUM, Parcels.wrap(model));
    }

    public static Intent goToSearch() {
        return new Intent(context, ActivitySearch.class);
    }

    public static Intent goToSpotifyPlaylist(Item item) {
        return new Intent(context, ActivitySpotify.class).putExtra(EXTRA.PLAYLIST, Parcels.wrap(item));
    }

    public static Intent goToSoundCloudPlaylist(SoundCloudPlaylist item) {
        return new Intent(context, ActivitySoundCloud.class).putExtra(EXTRA.PLAYLIST, Parcels.wrap(item));
    }

    public static Intent goToDeezerPlaylist(Playlist item) {
        return new Intent(context, ActivityDeezer.class).putExtra(EXTRA.PLAYLIST, Parcels.wrap(item));
    }
}
