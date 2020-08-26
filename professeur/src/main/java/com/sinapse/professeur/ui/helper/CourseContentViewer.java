package com.sinapse.professeur.ui.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.VideoView;

import com.sinapse.professeur.R;

import java.io.File;
import java.io.IOException;

public class CourseContentViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content_viewer);
        String path = getIntent().getExtras().getString("path");
        File file = new File(path);
        Uri uri = getIntent().getData();
        //Log.d("contentUri", uri.getPath());
        if(path.contains(".jpeg")){
            ImageView img = findViewById(R.id.img);
            img.setImageURI(uri);
        }else{
        //if(uri.getPath().contains("/video.")){
            final VideoView video = findViewById(R.id.video);
            video.setVideoURI(uri);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(video);
            video.setMediaController(mediaController);
            video.start();
        }

    }
}