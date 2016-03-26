package com.startogamu.musicalarm.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by josh on 23/03/16.
 */
@Data
public class SpotifyToken {

    @SerializedName("access_token")
    protected String accessToken;
    @SerializedName("token_type")
    protected String tokenType;
    @SerializedName("expires_in")
    protected int expiresIn;
    @SerializedName("refresh_token")
    protected String refreshToken;
}
