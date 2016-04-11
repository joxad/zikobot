package com.startogamu.musicalarm.module.spotifyauth.object;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class SpotifyRefreshToken {

    @SerializedName("grant_type")
    private final String grantType;
    @SerializedName("refresh_token")
    private final String refreshToken;
    @SerializedName("redirect_uri")
    private final String redirectUri;
    @SerializedName("client_id")
    private final String cliendId;
    @SerializedName("client_secret")
    private final String clientSecret;
}
