package com.startogamu.zikobot.core.module.spotify_api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by josh on 26/08/16.
 */
public class Albums {

    @SerializedName("items")
    public ArrayList<Item> items;

}

