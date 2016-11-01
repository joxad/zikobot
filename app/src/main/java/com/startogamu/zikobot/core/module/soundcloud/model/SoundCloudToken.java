package com.startogamu.zikobot.core.module.soundcloud.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 15/06/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SoundCloudToken {

    @SerializedName("access_token")
    private final String accessToken;
    @SerializedName("scope")
    private final String scope;
}
