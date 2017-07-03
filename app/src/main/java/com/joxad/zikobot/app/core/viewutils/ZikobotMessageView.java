package com.joxad.zikobot.app.core.viewutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joxad.zikobot.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manon on 05/06/16.
 */
public class ZikobotMessageView extends RelativeLayout {

    @BindView(R.id.tv_message)
    TextView tvMessage;

    public ZikobotMessageView(Context context) {
        super(context);
        init(null);
    }

    public ZikobotMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ZikobotMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_zikobot_message, this, true);
        if (isInEditMode()) {
            return;
        }
        ButterKnife.setDebug(true);

        ButterKnife.bind(this, view);
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs, R.styleable.ZikobotMessageView, 0, 0);
            tvMessage.setText(a.getString(R.styleable.ZikobotMessageView_zmv_message));
        }

    }

    public void setZmvMessage(String message) {
        tvMessage.setText(message);
    }
}
