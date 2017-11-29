package com.startogamu.zikobot.home.reco;

import android.content.Context;

import com.joxad.androidtemplate.core.log.AppLog;
import com.joxad.zikobot.data.db.ArtistManager;
import com.joxad.zikobot.data.db.model.ZikoArtist;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.home.artists.ArtistVM;

import java.util.Random;

import io.reactivex.functions.Consumer;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by blackbird-linux on 29/11/17.
 */

public class ArtistSimilarRecoCardContentVM extends RecoCardContentVM<ArtistVM> {

    public ItemBinding itemBinding = ItemBinding.of(BR.artistVM, R.layout.artist_item);
    /***

     * @param context
     */
    public ArtistSimilarRecoCardContentVM(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ArtistManager.INSTANCE.findAllSpotify().flatMap(items -> {
            int random = new Random().nextInt(items.size());
            return ArtistManager.INSTANCE.findSimilarArtists(items.get(random).getSpotifyId()).singleOrError();
        }).subscribe(zikoArtists -> {
            for (ZikoArtist zikoArtist : zikoArtists) {
                items.add(new ArtistVM(context, zikoArtist));
            }
        }, throwable -> AppLog.INSTANCE.e("Reco", throwable.getLocalizedMessage()));
    }
}
