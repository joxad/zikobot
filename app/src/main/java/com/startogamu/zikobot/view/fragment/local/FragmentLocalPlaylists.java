package com.startogamu.zikobot.view.fragment.local;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.navigation_manager.EventCollapseToolbar;
import com.startogamu.zikobot.core.event.navigation_manager.EventTabBars;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 09/06/16.
 */
public class FragmentLocalPlaylists extends Fragment {

    private static final String TAG = "FragmentLocalPlaylists";

    public static FragmentLocalPlaylists newInstance() {

        Bundle args = new Bundle();

        FragmentLocalPlaylists fragment = new FragmentLocalPlaylists();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_local_playlists, container, false);
        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new EventCollapseToolbar(getString(R.string.activity_music_title), null));
        EventBus.getDefault().post(new EventTabBars(true, TAG));

    }
}
