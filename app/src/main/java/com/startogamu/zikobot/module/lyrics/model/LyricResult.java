package com.startogamu.zikobot.module.lyrics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class LyricResult {

    @SerializedName("result")
    @Expose
    private Result result;

}