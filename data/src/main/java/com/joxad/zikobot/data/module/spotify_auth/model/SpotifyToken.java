package com.joxad.zikobot.data.module.spotify_auth.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
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
