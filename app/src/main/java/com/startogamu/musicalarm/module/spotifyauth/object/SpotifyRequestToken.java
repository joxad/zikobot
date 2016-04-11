package com.startogamu.musicalarm.module.spotifyauth.object;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author Fabien Baron
 * @company Phoceis
 * @email fbaron@phoceis.com
 * @date 11/04/2016
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
