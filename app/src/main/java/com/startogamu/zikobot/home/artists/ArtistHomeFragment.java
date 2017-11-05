package com.startogamu.zikobot.home.artists;

import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.databinding.ArtistHomeFragmentBinding;
import android.os.Bundle;

import android.support.annotation.Nullable;
/**
 * Generated by generator-android-template
 */
public class ArtistHomeFragment extends FragmentBase<ArtistHomeFragmentBinding, ArtistHomeFragmentVM> {


  public static ArtistHomeFragment newInstance() {
       Bundle args = new Bundle();
       ArtistHomeFragment fragment = new ArtistHomeFragment();
       fragment.setArguments(args);
       return fragment;
   }
    @Override
    public int data() {
        return BR.artistHomeFragmentVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.artist_home_fragment;
    }

    @Override
    public ArtistHomeFragmentVM baseFragmentVM(ArtistHomeFragmentBinding binding,@Nullable Bundle savedInstance) {
        return new ArtistHomeFragmentVM(this, binding,savedInstance);
    }
}
