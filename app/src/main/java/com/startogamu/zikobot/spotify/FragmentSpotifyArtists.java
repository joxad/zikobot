package com.startogamu.zikobot.spotify;

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
public class FragmentSpotifyArtists extends Fragment {

    public static FragmentSpotifyArtists newInstance() {

        Bundle args = new Bundle();

        FragmentSpotifyArtists fragment = new FragmentSpotifyArtists();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_spotify_artists, container,false);
        return viewGroup;
    }
}
