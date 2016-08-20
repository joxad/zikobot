package com.startogamu.zikobot.localnetwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.zikobot.R;

/**
 * Created by josh on 19/08/16.
 */
public class FragmentLocalNetwork extends Fragment {
    public static FragmentLocalNetwork newInstance() {
        Bundle args = new Bundle();
        FragmentLocalNetwork fragment = new FragmentLocalNetwork();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_network, container,false);

        getActivity().startService(new Intent(getContext(),LocalNetworkDiscoveryService.class));
        return view;
    }


}
