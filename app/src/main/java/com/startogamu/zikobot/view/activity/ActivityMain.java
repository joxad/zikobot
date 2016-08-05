package com.startogamu.zikobot.view.activity;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.fragmentmanager.FragmentManager;
import com.startogamu.zikobot.core.utils.ISearch;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.viewmodel.activity.ActivityMainVM;

/**
 * Created by josh on 08/06/16.
 */

public class ActivityMain extends ActivityBase<ActivityMainBinding, ActivityMainVM> {
    @Nullable
    @InjectExtra
    String fromWidget;

    @Override
    public int data() {
        return BR.activityMainVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_main;
    }

    @Override
    public ActivityMainVM baseActivityVM(ActivityMainBinding binding, Bundle savedInstanceState) {
        Dart.inject(this);

        return new ActivityMainVM(this, binding);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        vm.onPostCreate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchViewAction = (SearchView) MenuItemCompat
                .getActionView(searchMenuItem);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager
                .getSearchableInfo(getComponentName());
        searchViewAction.setSearchableInfo(searchableInfo);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(ActivityMain.this, "onMenuItemActionExpand called", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(ActivityMain.this, "onMenutItemActionCollapse called", Toast.LENGTH_SHORT).show();
                onBackPressed();
                return true;
            }
        });
        searchViewAction.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Logger.d(newText);
                if (FragmentManager.currentFragment() instanceof ISearch) {
                    ((ISearch) FragmentManager.currentFragment()).query(newText);
                }
                return false;
            }
        });
        searchViewAction.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (vm.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
