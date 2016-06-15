
package com.startogamu.zikobot.module.spotify_api.model;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Data;

@Generated("org.jsonschema2pojo")
@Data
@Parcel
public class ExternalUrls {

    @SerializedName("spotify")
    public String spotify;

    public ExternalUrls() {}
}
