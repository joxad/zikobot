package com.startogamu.musicalarm.module.spotify_auth.object;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author Fabien Baron
 * @company Phoceis
 * @email fbaron@phoceis.com
 * @date 11/04/2016
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
