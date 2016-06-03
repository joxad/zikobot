package com.startogamu.musicalarm.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.musicalarm.R;

/**
 * Created by josh on 30/05/16.
 */
public class DeezerFragment extends Fragment {

    public static DeezerFragment newInstance() {

        Bundle args = new Bundle();

        DeezerFragment fragment = new DeezerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deezer, container,false);
        return view;
    }
}
