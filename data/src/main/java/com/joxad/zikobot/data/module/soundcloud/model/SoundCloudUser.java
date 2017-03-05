package com.joxad.zikobot.data.module.soundcloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 15/06/16.
 */
@Parcel
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SoundCloudUser {


    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("permalink")
    @Expose
    public String permalink;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("uri")
    @Expose
    public String uri;
    @SerializedName("permalink_url")
    @Expose
    public String permalinkUrl;
    @SerializedName("avatar_url")
    @Expose
    public String avatarUrl;
    public SoundCloudUser() {
    }
}
