package com.startogamu.zikobot.view.custom;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ViewPlayerBinding;

/**
 * Created by josh on 08/06/16.
 */
public class ViewPlayer extends RelativeLayout {
    ViewPlayerBinding binding;
    public ViewPlayer(Context context) {
        super(context);
        init();
    }

    public ViewPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (isInEditMode()) {
            inflate(getContext(), R.layout.view_player, this);
            return;
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.view_player, this, true);
    }


}
