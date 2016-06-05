
package com.startogamu.zikobot.module.spotify_api.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Generated("org.jsonschema2pojo")
@Data
public class Tracks {

    @SerializedName("href")
    @Expose
    public String href;
    @SerializedName("total")
    @Expose
    public Integer total;

}
