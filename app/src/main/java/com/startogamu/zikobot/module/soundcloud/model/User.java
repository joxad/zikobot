
package com.startogamu.zikobot.module.soundcloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data public class User {

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

}
