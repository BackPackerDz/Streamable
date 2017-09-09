package com.squalala.streamable.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Fayçal KADDOURI on 01/06/16.
 * Email : powervlagos@gmail.com
 */
public class Session {

    private SharedPreferences preferences;
    private Context context;

    private final String KEY_LAST_VIDEO_URL = "last_url";


    public Session(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.context = context;
    }

    public String getLastVideoUrl() {
        return preferences.getString(KEY_LAST_VIDEO_URL, null);
    }

    public void setLastVideoUrl(String value) {
        preferences.edit().putString(KEY_LAST_VIDEO_URL, value).apply();
    }

}
