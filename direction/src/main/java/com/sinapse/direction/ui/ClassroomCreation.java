package com.sinapse.direction.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sinapse.direction.R;

public class ClassroomCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_creation);
    }

    public void onCreationClicked(View view) {
        Intent intent = new Intent(this, Grade.class);
        startActivity(intent);
    }
}
