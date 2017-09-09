package com.squalala.streamable.data.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fayçal KADDOURI on 02/06/16.
 */
public class BasicResponse {

    @SerializedName("shortcode")
    private String shortcode;
    @SerializedName("status")
    private int status;


    public String getShortcode() {
        return shortcode;
    }

    public int getStatus() {
        return status;
    }

    public String getUrl() {
        return "https://streamable.com/" + shortcode;
    }

    public String getMessage() {
        switch (status) {
            case 0: return "The video is being uploaded";
            case 1: return "The video is being processed";
            case 2: return "The video has at least one file ready";
            case 3: return "The video is unavailable due to an error";
        }
        return "Something gone wrong !";
    }
}
