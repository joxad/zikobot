package com.startogamu.zikobot.module.lyrics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 02/08/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class Result {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("articleUrl")
    @Expose
    private String articleUrl;
    @SerializedName("itunes")
    @Expose
    private String itunes;
    @SerializedName("lyrics")
    @Expose
    private String lyrics;
    @SerializedName("artist")
    @Expose
    private Artist artist;
    @SerializedName("album")
    @Expose
    private Album album;
    @SerializedName("small_image")
    @Expose
    private String smallImage;
    @SerializedName("medium_image")
    @Expose
    private String mediumImage;
    @SerializedName("large_image")
    @Expose
    private String largeImage;
}
