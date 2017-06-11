package com.joxad.zikobot.data.module.youtube;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoItem implements Parcelable {
    private String title;
    private String description;
    private String thumbnailURL;
    private String id;
    private String ref;
    private Long duration;

    public VideoItem() {
    }

    protected VideoItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        thumbnailURL = in.readString();
        id = in.readString();
        ref = in.readString();
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };

    public String getId() {
        return id;
    }
     
    public void setId(String id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getTitle() {
        return title;
    }
     
    public void setTitle(String title) {
        this.title = title;
    }
     
    public String getDescription() {
        return description;
    }
     
    public void setDescription(String description) {
        this.description = description;
    } 
     
    public String getThumbnailURL() {
        return thumbnailURL;
    }
     
    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;      
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(thumbnailURL);
        parcel.writeString(id);
        parcel.writeString(ref);
    }
}