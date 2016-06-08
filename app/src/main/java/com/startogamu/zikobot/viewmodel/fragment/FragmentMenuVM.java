package com.startogamu.zikobot.viewmodel.fragment;

import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.FragmentMenuBinding;
import com.startogamu.zikobot.view.Henson;
import com.startogamu.zikobot.view.fragment.FragmentMenu;

/**
 * Created by josh on 08/06/16.
 */
public class FragmentMenuVM extends FragmentBaseVM<FragmentMenu, FragmentMenuBinding> {
    /***
     * @param fragment
     * @param binding
     */
    public FragmentMenuVM(FragmentMenu fragment, FragmentMenuBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {
        binding.navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) item -> {
            switch (item.getItemId()){
                case R.id.menu_alarm:
                    fragment.startActivity(Henson.with(fragment.getContext()).gotoActivityAlarms().build());
                    return true;
            }
            return false;

        });
    }

    @Override
    public void destroy() {

    }
}
