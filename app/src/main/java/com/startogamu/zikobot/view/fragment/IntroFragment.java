package com.startogamu.zikobot.view.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;

import butterknife.Bind;

/**
 * Created by manon on 05/06/16.
 */
public class IntroFragment extends Fragment{


    @Bind(R.id.cv_header)
    CardView cvHeader;

    @Bind(R.id.iv_intro)
    RelativeLayout rl_image;
    public static IntroFragment newInstance(@LayoutRes int layoutRes) {
        Bundle args = new Bundle();
        IntroFragment fragment = new IntroFragment();
        args.putInt(EXTRA.LAYOUT, layoutRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layout = getArguments().getInt(EXTRA.LAYOUT);
        View view = inflater.inflate(layout,container, false);
        return view;
    }

}
