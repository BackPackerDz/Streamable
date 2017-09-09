package com.squalala.streamable.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.squalala.streamable.App;
import com.squalala.streamable.R;
import com.squalala.streamable.data.api.BasicResponse;
import com.squalala.streamable.data.api.services.StreamableService;
import com.squalala.streamable.data.prefs.Session;
import com.squalala.streamable.model.Video;

import java.io.File;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Fayçal KADDOURI on 07/09/17.
 */

public class UploadService extends Service {

    public static final String FILENAME = "path";
    public Video video;

    @Inject
    StreamableService service;

    @Inject
    Session session;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private final static int ID = 1;


    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();
        App.get(this).component().inject(this);
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }


    @DebugLog
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        video = (Video) intent.getSerializableExtra(FILENAME);

        System.out.println(video.toString());

        File file = new File(video.getPath());

        System.out.println("=========================== BEFORE ===========================");
        System.out.println("Name  : " + file.getName());
        System.out.println("Path  : " + file.getAbsolutePath());
        System.out.println("Size : " + humanReadableByteCount(file.length(), true));
        System.out.println("=========================== END ===========================");

        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                "file",
                file.getName(),
                RequestBody.create(MediaType.parse(video.getMediaType()), file));

        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), video.getTitle());


        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getBaseContext());

        PendingIntent notifyPIntent =
                PendingIntent.getService(getBaseContext(), 0, new Intent(), 0);

        mBuilder.setContentTitle(getBaseContext().getString(R.string.app_name))
                .setContentText("Upload in progress")
                .setTicker("Start uploading on Streamable")
                .setContentIntent(notifyPIntent)
                .setOngoing(true)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_stat_g4205_3);

        mBuilder.setProgress(0, 0, true);
        mNotifyManager.notify(ID, mBuilder.build());
     //   startForeground(ID, mBuilder.build());

        service.upload(filePart, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BasicResponse>() {

            @DebugLog
            @Override
            public void onNext(@NonNull BasicResponse basicResponse) {

                session.setLastVideoUrl(basicResponse.getUrl());

                // When the loop is finished, updates the notification
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, video.getTitle());
                i.putExtra(Intent.EXTRA_TEXT, basicResponse.getUrl());

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                getBaseContext(),
                                0,
                                i,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                if (basicResponse.getStatus() != 1)
                    return;

                mBuilder.setContentText("Upload finished")
                        .setContentIntent(resultPendingIntent)
                        .setTicker("Upload finished")
                        .setAutoCancel(true)
                        // Removes the progress bar
                        .setProgress(0,0,false);
                mNotifyManager.notify(ID, mBuilder.build());
             //   startForeground(ID, mBuilder.build());

                stopSelf();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

/*    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


    }

*/
}
