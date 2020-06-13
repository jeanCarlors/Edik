package com.sinapse.direction.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sinapse.direction.R;

public class Grade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
    }

    public void onClassRoomClicked(View view) {
        Intent intent = new Intent(this, GradeDetails.class);
        startActivity(intent);
    }

    public void onClassroomCreationClicked(View view) {
        Intent intent = new Intent(this, ClassroomCreation.class);
        startActivity(intent);
    }
}
