
package com.startogamu.zikobot.module.spotify_api.model;

import java.util.ArrayList;
import java.util.List;
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
public class Item {

    @SerializedName("collaborative")
    @Expose
    public Boolean collaborative;
    @SerializedName("external_urls")
    @Expose
    public ExternalUrls externalUrls;
    @SerializedName("href")
    @Expose
    public String href;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("images")
    @Expose
    public List<Image> images = new ArrayList<Image>();
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("owner")
    @Expose
    public Owner owner;
    @SerializedName("public")
    @Expose
    public Boolean _public;
    @SerializedName("snapshot_id")
    @Expose
    public String snapshotId;
    @SerializedName("tracks")
    @Expose
    public Tracks tracks;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("uri")
    @Expose
    public String uri;

    public Item(){}

}
