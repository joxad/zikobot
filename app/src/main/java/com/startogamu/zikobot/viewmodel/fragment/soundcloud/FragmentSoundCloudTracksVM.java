package com.startogamu.zikobot.viewmodel.fragment.soundcloud;

import android.databinding.ObservableArrayList;
import android.support.annotation.Nullable;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.core.event.SelectAllTracks;
import com.startogamu.zikobot.core.event.navigation_manager.EventCollapseToolbar;
import com.startogamu.zikobot.core.event.navigation_manager.EventTabBars;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventPlayListClicked;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentSoundCloudTracksBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudTrack;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.view.fragment.soundcloud.FragmentSoundCloudTracks;
import com.startogamu.zikobot.viewmodel.base.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.tatarka.bindingcollectionadapter.ItemView;


/***
 * {@link FragmentSoundCloudTracksVM} will call the apimanager to get the tracks of the playlist
 */
public abstract class FragmentSoundCloudTracksVM extends FragmentBaseVM<FragmentSoundCloudTracks, FragmentSoundCloudTracksBinding> {

    private static final String TAG = FragmentSoundCloudTracksVM.class.getSimpleName();
    public ObservableArrayList<TrackVM> items;

    public abstract ItemView getItemView();

    @Nullable
    @InjectExtra(EXTRA.PLAYLIST)
    SoundCloudPlaylist playlist;

    public FragmentSoundCloudTracksVM(FragmentSoundCloudTracks fragment, FragmentSoundCloudTracksBinding binding) {
        super(fragment, binding);
        Dart.inject(this, fragment.getArguments());
    }

    @Override
    public void init() {
        items = new ObservableArrayList<>();
    }

    /***
     * FInd the list of track from the playlist
     *
     * @param playlist
     */
    private void loadTracks(SoundCloudPlaylist playlist) {
        items.clear();
        for (SoundCloudTrack track : playlist.getSoundCloudTracks()) {

            items.add(new TrackVM(fragment.getContext(), Track.from(track)));
        }
    }

    @Subscribe
    public void onEvent(SelectAllTracks selectAllTracks) {
        for (TrackVM trackVM : items) {
            trackVM.select();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        if (playlist != null) {
            EventBus.getDefault().post(new EventCollapseToolbar(playlist.getTitle(), playlist.getSoundCloudTracks().get(0).getArtworkUrl()));
            EventBus.getDefault().post(new EventTabBars(false, TAG));
            loadTracks(playlist);
        } else {
            EventBus.getDefault().post(new EventCollapseToolbar(null, null));
            EventBus.getDefault().post(new EventTabBars(true, TAG));

        }
    }


    @Subscribe
    public void onEvent(EventPlayListClicked eventPlayListClicked) {
        EventBus.getDefault().post(new EventAddTrackToPlayer(items));
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void destroy() {
    }
}
