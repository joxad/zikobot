package com.joxad.zikobot.data.module.discogs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by linux on 7/25/17.
 */

public class DiscogsBaseSearchResult {

    @SerializedName("results")
    @Expose
    private List<DiscogsResult> results = null;


    public List<DiscogsResult> getResults() {
        return results;
    }

    public void setResults(List<DiscogsResult> results) {
        this.results = results;
    }

}



