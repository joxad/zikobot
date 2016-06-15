package com.startogamu.zikobot.module.soundcloud.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by josh on 15/06/16.
 */
@Data
public class SoundCloudToken {

    @SerializedName("access_token")
    private final String accessToken;
    @SerializedName("scope")
    private final String scope;
}
