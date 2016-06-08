package com.startogamu.zikobot.core.utils;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;

/**
 * Created by josh on 08/06/16.
 */
public class BottomViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {
    private int toolbarHeight;

    public BottomViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = ViewUtils.getToolbarHeight(context)*2;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationView fab, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, BottomNavigationView fab, View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;
            float ratio = (float) dependency.getY() / (float) toolbarHeight;
            fab.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }


}