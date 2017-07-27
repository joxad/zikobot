package com.joxad.zikobot.data.module.spotify_api.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ExternalUrls {

    @SerializedName("spotify")
    public String spotify;

    public ExternalUrls() {
    }
}
