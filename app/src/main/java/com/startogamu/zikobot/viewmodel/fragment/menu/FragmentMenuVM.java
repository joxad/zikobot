package com.startogamu.zikobot.viewmodel.fragment.menu;

import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.drawer.EventMenuDrawerAlarms;
import com.startogamu.zikobot.core.event.drawer.EventMenuDrawerLocal;
import com.startogamu.zikobot.core.event.drawer.EventMenuDrawerSpotify;
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
        binding.navigationView.setCheckedItem(R.id.menu_local);
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_local:
                    EventBus.getDefault().post(new EventMenuDrawerLocal());
                    return true;
                case R.id.menu_alarm:
                    EventBus.getDefault().post(new EventMenuDrawerAlarms());
                    return true;
                case R.id.menu_spotify:
                    EventBus.getDefault().post(new EventMenuDrawerSpotify());
                    return true;
                case R.id.action_settings:
                    fragment.getContext().startActivity(Henson.with(fragment.getContext()).gotoActivitySettings().build());
                    return false;
                case R.id.action_about:
                    new LibsBuilder()
                            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                            .withAboutAppName(fragment.getString(R.string.about))
                            .start(fragment.getActivity());
                    return false;
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
