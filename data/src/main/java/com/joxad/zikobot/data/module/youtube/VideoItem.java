package com.joxad.zikobot.data.module.youtube;

public class VideoItem {
    private String title;
    private String description;
    private String thumbnailURL;
    private String id;
    private String ref;
    private Long duration;
     
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
         
}