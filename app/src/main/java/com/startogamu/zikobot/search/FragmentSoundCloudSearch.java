package com.startogamu.zikobot.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.startogamu.zikobot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by josh on 25/08/16.
 */
public class FragmentSoundCloudSearch extends Fragment {


    @Bind(R.id.tv_search)
    TextView tvSearch;

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
        ButterKnife.bind(this,view);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        tvSearch.setText(SearchManager.QUERY);
    }

}