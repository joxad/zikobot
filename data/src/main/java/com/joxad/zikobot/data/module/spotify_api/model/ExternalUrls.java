package com.joxad.zikobot.data.module.spotify_api.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import javax.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Data;

@Generated("org.jsonschema2pojo")
@Data
@Parcel
@AllArgsConstructor(suppressConstructorProperties = true)
public class ExternalUrls {

    @SerializedName("spotify")
    public String spotify;

    public ExternalUrls() {
    }
}
