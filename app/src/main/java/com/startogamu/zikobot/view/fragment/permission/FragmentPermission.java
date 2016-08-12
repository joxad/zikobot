package com.startogamu.zikobot.view.fragment.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.f2prateek.dart.henson.Bundler;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.view.custom.ZikobotMessageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by josh on 10/06/16.
 */
public class FragmentPermission extends Fragment {

    @InjectExtra(EXTRA.MESSAGE)
    String message;
    @InjectExtra(EXTRA.PERMISSION)
    int permission;
    public static FragmentPermission newInstance(final String message, final int permission) {
        FragmentPermission fragment = new FragmentPermission();
        fragment.setArguments(Bundler.create().put(EXTRA.MESSAGE, message).put(EXTRA.PERMISSION, permission).get());
        return fragment;
    }

    @Bind(R.id.zmv)
    public ZikobotMessageView zmv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permission, container, false);
        ButterKnife.bind(this,view);
        Dart.inject(this, getArguments());
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
