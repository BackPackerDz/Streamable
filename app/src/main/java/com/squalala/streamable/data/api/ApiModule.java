package com.squalala.streamable.data.api;


import android.support.v4.util.LruCache;

import com.squalala.streamable.Constants;
import com.squalala.streamable.data.api.services.StreamableService;
import com.squalala.streamable.data.prefs.Session;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by faycal on 01/06/16.
 */
@Module
public class ApiModule {

    @Provides
    @Singleton
    LruCache<Class<?>, Observable<?>> provideApiObservables() {
        return new LruCache<>(10);
    }

    @Provides
    @Singleton
    Retrofit provideRestAdapter(Session session) {

      //  LoggingInterceptor interceptor = new LoggingInterceptor(session);
        HttpLoggingInterceptor interceptorHttp = new HttpLoggingInterceptor();
        // set your desired log level
        interceptorHttp.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        OkHttpClient client = builder
                .addInterceptor(interceptorHttp)
                .addInterceptor(new BasicAuthInterceptor())
                .build();

        return new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }


    @Provides
    @Singleton
    StreamableService provideStreamableService(Retrofit adapter) {
        return adapter.create(StreamableService.class);
    }


}
