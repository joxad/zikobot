package com.startogamu.zikobot.module.soundcloud.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by josh on 15/06/16.
 */
@Data
public class SoundCloudUser {

    @SerializedName("id")
    private final long id;

    @SerializedName("username")
    private final String userName;

    @SerializedName("avatar_url")
    private final String avatarUrl;

}
