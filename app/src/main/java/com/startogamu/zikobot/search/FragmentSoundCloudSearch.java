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
public class FragmentSoundCloudSearch extends Fragment {
    public static FragmentSoundCloudSearch newInstance() {
        Bundle args = new Bundle();
        FragmentSoundCloudSearch fragment = new FragmentSoundCloudSearch();
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