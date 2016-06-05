package com.startogamu.zikobot.viewmodel.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.REQUEST;
import com.startogamu.zikobot.databinding.FragmentLocalMusicBinding;
import com.startogamu.zikobot.module.alarm.model.AlarmTrack;
import com.startogamu.zikobot.module.alarm.model.LocalTrack;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.view.fragment.FragmentLocalMusic;
import com.startogamu.zikobot.viewmodel.base.TrackVM;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentLocalVM extends FragmentBaseVM<FragmentLocalMusic, FragmentLocalMusicBinding> {

    private static final String TAG = FragmentLocalVM.class.getSimpleName();
    public ObservableArrayList<TrackVM> items;
    public ItemView itemView = ItemView.of(BR.trackVM, R.layout.item_alarm_track);

    public ObservableBoolean showNoResult;

    /***
     * @param fragment
     * @param binding
     */
    public FragmentLocalVM(FragmentLocalMusic fragment, FragmentLocalMusicBinding binding) {
        super(fragment, binding);
    }


    @Override
    public void init() {
        showNoResult = new ObservableBoolean(false);
        items = new ObservableArrayList<>();
        Injector.INSTANCE.contentResolverComponent().init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST.PERMISSION_STORAGE);
            } else {
                loadLocalMusic();
            }
        } else {
            loadLocalMusic();
            // continue with your code
        }
    }

    public void loadLocalMusic() {
        Injector.INSTANCE.contentResolverComponent().localMusicManager().getLocalTracks().subscribe(localTracks -> {
            Log.d(TAG, "" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                items.add(new TrackVM(fragment.getContext(), AlarmTrack.from(localTrack)));
            }
            if (localTracks.isEmpty()) {
                showNoResult.set(true);
            }
        }, throwable -> {
            showNoResult.set(true);
        });
    }


    @Override
    public void destroy() {

    }
}
