
package com.startogamu.zikobot.module.spotify_api.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Generated("org.jsonschema2pojo")
@Data
@Parcel
@AllArgsConstructor(suppressConstructorProperties = true)
public class Owner {

    @SerializedName("external_urls")
    @Expose
    public ExternalUrls externalUrls;
    @SerializedName("href")
    @Expose
    public String href;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("uri")
    @Expose
    public String uri;

    public Owner() {}
}
