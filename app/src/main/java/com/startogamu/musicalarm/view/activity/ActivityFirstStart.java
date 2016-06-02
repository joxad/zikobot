package com.startogamu.musicalarm.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f2prateek.dart.HensonNavigable;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.AppPrefs;
import com.ugurtekbas.fadingindicatorlibrary.FadingIndicator;

import butterknife.Bind;

/**
 * Created by josh on 02/06/16.
 */
@HensonNavigable
public class ActivityFirstStart extends AppCompatActivity {

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Bind(R.id.circleIndicator)
    FadingIndicator circleIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppPrefs.saveFirstStart(true);
        setContentView(R.layout.activity_start);

        viewPager.setAdapter(new CustomPagerAdapter(this));
        circleIndicator.setViewPager(viewPager);
    }


    private class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;

        public CustomPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return CustomPagerEnum.values().length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
            return mContext.getString(customPagerEnum.getTitleResId());
        }

    }


    public enum CustomPagerEnum {

        RED(R.string.view_pager_1, R.layout.view_pager_1),
        BLUE(R.string.view_pager_2, R.layout.view_pager_2),
        ORANGE(R.string.view_pager_3, R.layout.view_pager_3);

        private int mTitleResId;
        private int mLayoutResId;

        CustomPagerEnum(int titleResId, int layoutResId) {
            mTitleResId = titleResId;
            mLayoutResId = layoutResId;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public int getLayoutResId() {
            return mLayoutResId;
        }

    }
}
