package com.squalala.streamable;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.squareup.leakcanary.LeakCanary;

public class App extends MultiDexApplication {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);


    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        component.inject(this);
    }

    public AppComponent component() {
        return component;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



}
