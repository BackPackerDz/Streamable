package com.squalala.streamable.model;

import java.io.Serializable;

/**
 * Created by Fayçal KADDOURI on 07/09/17.
 */

public class Video implements Serializable {

    private String title;
    private String path;
    private String mediaType;


    public Video(String title, String path, String mediaType) {
        this.title = title;
        this.path = path;
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }
}
