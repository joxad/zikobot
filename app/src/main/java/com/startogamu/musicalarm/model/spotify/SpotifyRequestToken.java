package com.startogamu.musicalarm.model.spotify;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by josh on 23/03/16.
 */
@Data
public class SpotifyRequestToken {

    @SerializedName("grant_type")
    private final String grantType;
    @SerializedName("code")
    private final String code;
    @SerializedName("redirect_uri")
    private final String redirectUri;
    @SerializedName("client_id")
    private final String cliendId;
    @SerializedName("client_secret")
    private final String clientSecret;
}
