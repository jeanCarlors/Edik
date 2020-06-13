package com.sinapse.direction.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sinapse.direction.R;

public class TeacherList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
    }

    public void onTeacherClicked(View view) {
        Intent intent = new Intent(this, TeacherProfile.class);
        startActivity(intent);
    }

    public void onActivityClicked(View view) {
        Intent intent = new Intent(this, ActivityList.class);
        startActivity(intent);
    }
}
