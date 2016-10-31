package com.oxylane.decathlon.core.events;

import lombok.Data;

/**
 * Created by Jocelyn on 27/10/2016.
 */
@Data
public class EventShowDrawer {
    public final static int FAVORITE = 1;
    public final static int CART = 2;

    protected final int type;
}
