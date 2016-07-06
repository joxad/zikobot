package com.startogamu.zikobot.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.startogamu.zikobot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by manon on 05/06/16.
 */
public class ZikobotMessageView extends RelativeLayout {

    @Bind(R.id.tv_message)
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
        if (isInEditMode()) {
            inflate(getContext(), R.layout.view_zikobot_message, this);
            return;
        } else {
            inflate(getContext(), R.layout.view_zikobot_message, this);

        }

        ButterKnife.bind(this);
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
