package com.startogamu.zikobot.core.event.permission;

import lombok.Data;

/**
 * Created by josh on 10/06/16.
 */
@Data
public class EventPermission {
    private final int permission;
    private final boolean granted;
}
