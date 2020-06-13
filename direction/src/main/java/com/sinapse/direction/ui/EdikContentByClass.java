package com.sinapse.direction.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sinapse.direction.R;

public class EdikContentByClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edik_content_by_class);
    }

    public void onNSOneClicked(View view) {
        Intent intent = new Intent(this, ContentList.class);
        startActivity(intent);
    }
}
