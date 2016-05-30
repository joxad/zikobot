package com.startogamu.musicalarm.viewmodel.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.base.IVM;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.REQUEST;
import com.startogamu.musicalarm.databinding.FragmentLocalMusicBinding;
import com.startogamu.musicalarm.module.alarm.object.LocalTrack;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.view.fragment.LocalMusicFragment;
import com.startogamu.musicalarm.viewmodel.items.ItemLocalTrackViewModel;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscriber;

/**
 * Created by josh on 26/03/16.
 */
public class LocalMusicViewModel extends BaseObservable implements IVM {

    private static final String TAG = LocalMusicViewModel.class.getSimpleName();
    private LocalMusicFragment fragment;
    private FragmentLocalMusicBinding binding;
    private final ObservableArrayList<ItemLocalTrackViewModel> localTrackViewModels = new ObservableArrayList<>();
    public final ObservableBoolean showProgress = new ObservableBoolean(true);

    public final ObservableBoolean showNoResult = new ObservableBoolean(false);

    @Bindable
    public ObservableArrayList<ItemLocalTrackViewModel> getItems() {
        return localTrackViewModels;
    }

    public ItemView itemView = ItemView.of(BR.itemLocalTrackViewModel, R.layout.item_local_track);

    public LocalMusicViewModel(LocalMusicFragment fragment, FragmentLocalMusicBinding binding) {
        this.fragment = fragment;
        this.binding = binding;
        init();
    }

    public void loadLocalMusic() {
        Injector.INSTANCE.contentResolverComponent().localMusicManager().getLocalTracks().subscribe(new Subscriber<List<LocalTrack>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "" + e.getLocalizedMessage());

            }

            @Override
            public void onNext(List<LocalTrack> localTracks) {
                Log.d(TAG, "" + localTracks.size());
                showProgress.set(false);

                for (LocalTrack localTrack : localTracks) {
                    localTrackViewModels.add(new ItemLocalTrackViewModel(fragment, localTrack));
                }
                if (localTrackViewModels.isEmpty()) {
                    showProgress.set(true);
                }
            }
        });
    }


    @Override
    public void init() {
        Injector.INSTANCE.contentResolverComponent().init(this);
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

    @Override
    public void destroy() {

    }
}
