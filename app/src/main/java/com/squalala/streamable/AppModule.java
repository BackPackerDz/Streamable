package com.squalala.streamable;

import android.app.Application;
import android.content.Context;


import com.squalala.streamable.data.prefs.Session;
import com.squalala.streamable.utils.NotificationUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by faycal on 01/06/16.
 */
@Module
public class AppModule {

    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    public Session provideSession() {
        return new Session(app.getApplicationContext());
    }


    @Provides
    @Singleton
    public NotificationUtils provideNotificationUtils() {
        return new NotificationUtils(app.getApplicationContext());
    }

}
