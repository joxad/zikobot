package com.startogamu.zikobot.view.fragment.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.view.custom.ZikobotMessageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by josh on 10/06/16.
 */
public class FragmentPermission extends Fragment {

    String message;
    int permission;
    public static FragmentPermission newInstance(final String message, final int permission) {
        FragmentPermission fragment = new FragmentPermission();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA.MESSAGE, message);
        bundle.putInt(EXTRA.PERMISSION, permission);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Bind(R.id.zmv)
    public ZikobotMessageView zmv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permission, container, false);
        ButterKnife.bind(this,view);
        message = getArguments().getString(EXTRA.MESSAGE);
        permission = getArguments().getInt(EXTRA.PERMISSION);
        zmv.setZmvMessage(message);
        zmv.setOnClickListener(v -> askPermission());
        return view;
    }

    /***
     * Method to ask storage perm
     */
    private void askPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, permission);

    }
}
