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
import com.startogamu.zikobot.module.content_resolver.model.LocalTrack;
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

    public ObservableBoolean showZmvMessage;

    public String zmvMessage;

    /***
     * @param fragment
     * @param binding
     */
    public FragmentLocalVM(FragmentLocalMusic fragment, FragmentLocalMusicBinding binding) {
        super(fragment, binding);
    }


    @Override
    public void init() {
        showZmvMessage = new ObservableBoolean(false);
        zmvMessage = "";
        items = new ObservableArrayList<>();
        Injector.INSTANCE.contentResolverComponent().init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                askPermission();
             } else {
                loadLocalMusic();
            }
        } else {
            loadLocalMusic();
            // continue with your code
        }
    }

    /***
     * Method to ask storage perm
     */
    private void askPermission() {
        ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST.PERMISSION_STORAGE);

    }

    /***
     * Load the local music
     */
    public void loadLocalMusic() {
        Injector.INSTANCE.contentResolverComponent().localMusicManager().getLocalTracks().subscribe(localTracks -> {
            Log.d(TAG, "" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                items.add(new TrackVM(fragment.getContext(), AlarmTrack.from(localTrack)));
            }
            if (localTracks.isEmpty()) {
                updateMessage(fragment.getString(R.string.no_music));
            } else {
                showZmvMessage.set(false);
            }
        }, throwable -> {
            updateMessage(fragment.getString(R.string.no_music));

        });
    }
    /***
     * Update t
     *
     * @param string
     */
    private void updateMessage(String string) {
        showZmvMessage.set(true);
        zmvMessage = string;
        binding.zmv.setZmvMessage(zmvMessage);
    }

    public void permissionDenied() {
        updateMessage(fragment.getString(R.string.permission_local));
        binding.zmv.setOnClickListener(v -> askPermission());
    }




    @Override
    public void destroy() {

    }


}
