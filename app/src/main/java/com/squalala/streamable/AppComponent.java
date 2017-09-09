package com.squalala.streamable;


import com.squalala.streamable.data.api.ApiModule;
import com.squalala.streamable.data.prefs.Session;
import com.squalala.streamable.services.UploadService;
import com.squalala.streamable.utils.NotificationUtils;

import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by faycal on 01/06/16.
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
                ApiModule.class
        }
)
public interface AppComponent {
    void inject(App app);
    void inject(UploadService uploadService);

    CompositeDisposable geCompositeDisposable();
    Session getSession();
    NotificationUtils getNotificationUtils();
}
