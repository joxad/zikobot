package com.joxad.zikobot.data.module.soundcloud.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

public class SoundCloudToken {

    @SerializedName("access_token")
    final String accessToken;
    @SerializedName("scope")
    final String scope;

    public SoundCloudToken(String accessToken, String scope) {
        this.accessToken = accessToken;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return scope;
    }
}
