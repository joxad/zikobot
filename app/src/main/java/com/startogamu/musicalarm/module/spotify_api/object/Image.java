package com.startogamu.musicalarm.module.spotify_api.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Image {

    @SerializedName("height")
    @Expose
    public Integer height;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("width")
    @Expose
    public Integer width;

}
