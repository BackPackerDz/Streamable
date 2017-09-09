package com.squalala.streamable.ui.upload;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squalala.streamable.R;
import com.squalala.streamable.data.prefs.Session;
import com.squalala.streamable.model.Video;
import com.squalala.streamable.services.UploadService;
import com.squalala.streamable.utils.ConnectionDetector;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

/**
 * Created by Fayçal KADDOURI on 07/09/17.
 */

public class UploadActivity extends AppCompatActivity
    implements PermissionListener {

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 11;

    @BindView(R.id.editTitle)
    EditText editTitle;

    @BindView(R.id.textLastUpload)
    TextView txtUrlLastUpload;

    private Session session;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);

        session = new Session(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (session.getLastVideoUrl() != null)
            txtUrlLastUpload.setText(session.getLastVideoUrl());
        else
            txtUrlLastUpload.setText("Nothing for the moment...");
    }

    private String getTitleVideo() {
        return editTitle.getText().toString().trim();
    }

    @OnClick(R.id.button2)
    void onClickSelectVideo() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(this)
                .check();
    }

    private void selectVideoFromGallery() {
        Intent intent =  new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent,"Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
    }

    @DebugLog
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();

                String path = getPath(selectedImageUri);
                File file = new File(path);

                if (file.length() == 0) {
                    Toast.makeText(UploadActivity.this, "We can't upload this video", Toast.LENGTH_LONG).show();
                    return;
                }

                launchUploadService(path, getContentResolver().getType(selectedImageUri));
            }
        }
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    // UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @DebugLog
    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        if (ConnectionDetector.isConnectingToInternet(this))
            selectVideoFromGallery();
        else
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {}

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {}

    public void launchUploadService(String path, String mediaType) {
        Video video = new Video(getTitleVideo(), path, mediaType);
        Intent i = new Intent(this, UploadService.class);
        i.putExtra(UploadService.FILENAME, video);
        startService(i);
        finish();
    }





}
