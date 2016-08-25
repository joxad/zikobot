package com.startogamu.zikobot.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.zikobot.R;

/**
 * Created by josh on 25/08/16.
 */
public class FragmentDeezerSearch extends Fragment {
    public static FragmentDeezerSearch newInstance() {
        Bundle args = new Bundle();
        FragmentDeezerSearch fragment = new FragmentDeezerSearch();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_sound_cloud_search,container, false);

        return view;
    }


}