
package com.startogamu.musicalarm.module.spotify_api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import lombok.Data;

@Generated("org.jsonschema2pojo")
@Data
public class SpotifyAlbum {

    @SerializedName("id")
    private String id;

    @SerializedName("images")
    public List<Image> images = new ArrayList<Image>();

    @SerializedName("limit")
    public Integer limit;

    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public String type;
    @SerializedName("album_type")
    protected String albumType;
}
