package com.startogamu.zikobot.view.fragment.spotify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.zikobot.R;

/**
 * Created by josh on 09/06/16.
 */
public class FragmentSpotifyAlbums extends Fragment {

    public static FragmentSpotifyAlbums newInstance() {

        Bundle args = new Bundle();

        FragmentSpotifyAlbums fragment = new FragmentSpotifyAlbums();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_spotify_albums, container,false);
        return viewGroup;
    }
}
