package com.joxad.zikobot.app.core.fragmentmanager;

import android.content.Context;
import android.content.Intent;

import com.deezer.sdk.model.Playlist;
import com.joxad.zikobot.data.model.Alarm;
import com.joxad.zikobot.data.model.Album;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.search.ActivitySearch;
import com.joxad.zikobot.app.spotify.ActivitySpotify;
import com.joxad.zikobot.app.album.ActivityAlbum;
import com.joxad.zikobot.app.artist.ActivityArtist;
import com.joxad.zikobot.app.deezer.ActivityDeezer;
import com.joxad.zikobot.app.localnetwork.ActivityLocalNetwork;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.joxad.zikobot.data.module.spotify_api.model.Item;
import com.joxad.zikobot.data.model.Artist;
import com.joxad.zikobot.app.settings.ActivitySettings;
import com.joxad.zikobot.app.soundcloud.ActivitySoundCloud;
import com.joxad.zikobot.app.alarm.ActivityAlarm;
import com.joxad.zikobot.app.home.ActivityMain;
import com.joxad.zikobot.app.wakeup.ActivityWakeUp;

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
        return new Intent(context, ActivityMain.class);
    }

    public static Intent goToSettings() {
        return new Intent(context, ActivitySettings.class);
    }

    public static Intent goToWakeUp(Alarm alarm) {
        return new Intent(context, ActivityWakeUp.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(EXTRA.ALARM, Parcels.wrap(alarm));
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

    public static Intent goToLocalNetwork(String currentMedia) {
        return new Intent(context, ActivityLocalNetwork.class).putExtra(EXTRA.MEDIA, currentMedia);
    }
}
