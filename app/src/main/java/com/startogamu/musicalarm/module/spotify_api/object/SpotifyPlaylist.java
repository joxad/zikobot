
package com.startogamu.musicalarm.module.spotify_api.object;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Generated("org.jsonschema2pojo")
@Data
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
