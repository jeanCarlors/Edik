package com.sinapse.direction.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sinapse.direction.R;

public class ContentByChapter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_by_chapter);
    }

    public void onChapterClicked(View view) {
        Intent intent = new Intent(this, ContentDetails.class);
        startActivity(intent);
    }
}
