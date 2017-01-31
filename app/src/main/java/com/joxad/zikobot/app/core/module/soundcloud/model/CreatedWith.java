
package com.joxad.zikobot.app.core.module.soundcloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Parcel
@Data
public class CreatedWith {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("uri")
    @Expose
    public String uri;
    @SerializedName("permalink_url")
    @Expose
    public String permalinkUrl;
    @SerializedName("external_url")
    @Expose
    public String externalUrl;

}
