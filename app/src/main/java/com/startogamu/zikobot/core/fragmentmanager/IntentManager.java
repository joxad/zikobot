package com.startogamu.zikobot.core.fragmentmanager;

import android.content.Context;
import android.content.Intent;

import com.startogamu.zikobot.album.ActivityAlbum;
import com.startogamu.zikobot.artist.ActivityArtist;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.spotify_api.model.ExternalUrls;
import com.startogamu.zikobot.module.zikobot.model.Alarm;
import com.startogamu.zikobot.module.zikobot.model.Album;
import com.startogamu.zikobot.module.zikobot.model.Artist;
import com.startogamu.zikobot.view.Henson;

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
        return Henson.with(context).gotoActivityAlarm().alarm(alarm).build();

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

    public static Intent goToMusic(Alarm alarm) {
        return Henson.with(context).gotoActivityMusic().alarm(alarm).build();
    }

    public static Intent goToAlbum(Album model) {
        Intent intent = new Intent(context, ActivityAlbum.class);
        intent.putExtra(EXTRA.LOCAL_ALBUM, Parcels.wrap(model));
        return intent;
    }
}
