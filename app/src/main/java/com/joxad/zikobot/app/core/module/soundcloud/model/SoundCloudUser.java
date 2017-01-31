package com.joxad.zikobot.app.core.module.soundcloud.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 15/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SoundCloudUser {

    @SerializedName("id")
    private final long id;

    @SerializedName("username")
    private final String userName;

    @SerializedName("avatar_url")
    private final String avatarUrl;

}
