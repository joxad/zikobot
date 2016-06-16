package com.startogamu.zikobot.core.event.navigation_manager;

import com.startogamu.zikobot.core.fragmentmanager.NavigationManager;

import lombok.Data;

/**
 * Created by josh on 16/06/16.
 */
@Data
public class EventAccountSelect {
    final NavigationManager.Account account;
}
