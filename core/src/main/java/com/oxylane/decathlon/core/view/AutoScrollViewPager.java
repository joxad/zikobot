package com.oxylane.decathlon.core.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by josh on 26/08/16.
 */
public class AutoScrollViewPager extends ViewPager {

    Timer timer;
    Handler uiHandler;
    int pagerCurrentState;


    public AutoScrollViewPager(Context context) {
        super(context);
        init();
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        uiHandler = new Handler();
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                pagerCurrentState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    initNewsTimer();
                }
            }
        });
    }

    public void onResume() {

        initNewsTimer();
    }

    private void initNewsTimer() {
        if (timer != null)
            timer.cancel();

        timer = new Timer("NewsTimer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("timer", "timer " + getCurrentItem());
                        if (getAdapter().getCount() > 1 && pagerCurrentState == ViewPager.SCROLL_STATE_IDLE) {
                            Log.e("timer","dans le if  " + getCurrentItem());
                            getAdapter().notifyDataSetChanged();
                            int currentItem = getCurrentItem();
                            int nextItem = currentItem == getAdapter().getCount() - 1 ? 0 : currentItem + 1;
                            setCurrentItem(nextItem, true);
                            getAdapter().notifyDataSetChanged();
                            Log.e("timer","end timer " + currentItem + " - " + nextItem);
                        }
                    }
                });
            }
        }, 4000);
    }

    public void onPause() {
        timer.cancel();
        timer = null;
    }

}
