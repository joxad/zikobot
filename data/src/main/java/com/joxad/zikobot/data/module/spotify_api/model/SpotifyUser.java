package com.joxad.zikobot.data.module.spotify_api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 11/03/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SpotifyUser  {

    @SerializedName("country")
    public String country;
    @SerializedName("display_name")
    public String displayName;
    @SerializedName("href")
    public String href;
    @SerializedName("id")
    public String id;
    @SerializedName("images")
    public List<Image> images = new ArrayList<Image>();
    @SerializedName("product")
    public String product;
    @SerializedName("type")
    public String type;
    @SerializedName("uri")
    public String uri;
}
