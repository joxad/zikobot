package com.startogamu.zikobot.core;

import com.startogamu.zikobot.home.albums.AlbumVM;
import com.startogamu.zikobot.home.artists.ArtistVM;
import com.viethoa.RecyclerViewFastScroller;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * Created by Jocelyn on 08/09/2017.
 */

public class AlphabeticalAdapter<T> extends BindingRecyclerViewAdapter<T> implements RecyclerViewFastScroller.BubbleTextGetter {

    @Override
    public String getTextToShowInBubble(int pos) {
        T t = getAdapterItem(pos);
        String s = "";
        if (t.getClass() == ArtistVM.class) {
            ArtistVM artistVM = (ArtistVM) t;
            s = artistVM.getName().substring(0, 1);
        } else if (t.getClass() == AlbumVM.class) {
            AlbumVM albumVM = (AlbumVM) t;
            s = albumVM.getName().substring(0, 1);
        }
        return s;
    }
}
