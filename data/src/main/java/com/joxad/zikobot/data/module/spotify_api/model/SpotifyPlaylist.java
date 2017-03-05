package com.joxad.zikobot.data.module.spotify_api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Data;

@Generated("org.jsonschema2pojo")
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SpotifyPlaylist {

    @SerializedName("href")
    @Expose
    public String href;
    @SerializedName("items")
    @Expose
    public List<Item> items = new ArrayList<Item>();
    @SerializedName("limit")
    @Expose
    public Integer limit;
    @SerializedName("next")
    @Expose
    public Object next;
    @SerializedName("offset")
    @Expose
    public Integer offset;
    @SerializedName("previous")
    @Expose
    public Object previous;
    @SerializedName("total")
    @Expose
    public Integer total;

}
