
package com.startogamu.zikobot.module.spotify_api.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@Generated("org.jsonschema2pojo")
@Data
@Parcel
@AllArgsConstructor(suppressConstructorProperties = true)

public class Tracks {

    @SerializedName("href")
    @Expose
    public String href;
    @SerializedName("total")
    @Expose
    public Integer total;

    @SerializedName("items")
    public ArrayList<SpotifyTrack> items;
    public Tracks() {}
}
