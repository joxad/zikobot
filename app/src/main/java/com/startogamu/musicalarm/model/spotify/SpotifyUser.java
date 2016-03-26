package com.startogamu.musicalarm.model.spotify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by josh on 11/03/16.
 */
@Data
public class SpotifyUser {

    @SerializedName("country")
    final String country;
    @SerializedName("display_name")
    final String displayName;
    @SerializedName("href")
    final String href;
    @SerializedName("id")
    final String id;
    @SerializedName("images")
    final List<SpotifyImage> images;
    @SerializedName("product")
    final String product;
    @SerializedName("type")
    final String type;
    @SerializedName("uri")
    final String uri;

    @Data
    public class SpotifyImage {
        @SerializedName("url")
        final String url;
    }
}
