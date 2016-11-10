package com.startogamu.zikobot.core.fragmentmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.WindowManager;

import com.deezer.sdk.model.Playlist;
import com.startogamu.zikobot.album.ActivityAlbum;
import com.startogamu.zikobot.artist.ActivityArtist;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.deezer.ActivityDeezer;
import com.startogamu.zikobot.localnetwork.ActivityLocalNetwork;
import com.startogamu.zikobot.core.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.core.module.spotify_api.model.Item;
import com.startogamu.zikobot.core.model.Alarm;
import com.startogamu.zikobot.core.model.Album;
import com.startogamu.zikobot.core.model.Artist;
import com.startogamu.zikobot.search.ActivitySearch;
import com.startogamu.zikobot.settings.ActivitySettings;
import com.startogamu.zikobot.soundcloud.ActivitySoundCloud;
import com.startogamu.zikobot.spotify.ActivitySpotify;
import com.startogamu.zikobot.alarm.ActivityAlarm;
import com.startogamu.zikobot.intro.ActivityFirstStart;
import com.startogamu.zikobot.home.ActivityMain;
import com.startogamu.zikobot.wakeup.ActivityWakeUp;

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

    public static Intent goToTuto() {
        return new Intent(context, ActivityFirstStart.class);
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
