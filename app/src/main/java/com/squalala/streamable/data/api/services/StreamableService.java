package com.squalala.streamable.data.api.services;

import com.squalala.streamable.data.api.BasicResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Fayçal KADDOURI on 07/09/17.
 */

public interface StreamableService {

    @Multipart
    @POST("upload")
    Observable<BasicResponse> upload(@Part MultipartBody.Part videoToUpload, @Part("title") RequestBody title);
}
