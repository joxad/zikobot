package com.startogamu.zikobot.viewmodel.fragment.menu;

import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventMenuDrawerAbout;
import com.startogamu.zikobot.core.event.EventMenuDrawerAlarms;
import com.startogamu.zikobot.core.event.EventMenuDrawerLocal;
import com.startogamu.zikobot.databinding.FragmentMenuBinding;
import com.startogamu.zikobot.view.Henson;
import com.startogamu.zikobot.view.fragment.menu.FragmentMenu;

import org.greenrobot.eventbus.EventBus;

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
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_local:
                    EventBus.getDefault().post(new EventMenuDrawerLocal());
                    return true;
                case R.id.menu_alarm:
                    EventBus.getDefault().post(new EventMenuDrawerAlarms());
                    return true;
                case R.id.action_settings:
                    fragment.getContext().startActivity(Henson.with(fragment.getContext()).gotoActivitySettings().build());
                    return true;
                case R.id.action_about:
                    EventBus.getDefault().post(new EventMenuDrawerAbout());
                    return true;
            }
            return false;

        });

/*

    */
    }

    @Override
    public void destroy() {

    }
}
