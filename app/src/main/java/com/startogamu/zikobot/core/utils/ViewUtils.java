package com.startogamu.zikobot.core.utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.startogamu.zikobot.R;

/**
 * Created by josh on 08/06/16.
 */
public class ViewUtils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

}
