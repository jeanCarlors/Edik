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

    public void onNSIClicked(View view) {
        Intent intent = new Intent(this, ContentList.class);
        intent.putExtra("grade", "/NS I");
        startActivity(intent);
    }

    public void onNSIIClicked(View view) {
        Intent intent = new Intent(this, ContentList.class);
        intent.putExtra("grade", "/NS II");
        startActivity(intent);
    }

    public void onNSIIIClicked(View view) {
        Intent intent = new Intent(this, ContentList.class);
        intent.putExtra("grade", "/NS III");
        startActivity(intent);
    }

    public void onNSIVClicked(View view) {
        Intent intent = new Intent(this, ContentList.class);
        intent.putExtra("grade", "/NS IV");
        startActivity(intent);
    }
}
