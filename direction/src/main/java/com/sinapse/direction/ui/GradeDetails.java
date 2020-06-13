package com.sinapse.direction.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sinapse.direction.R;

public class GradeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_details);
    }

    public void onStudentClicked(View view) {
        Intent intent = new Intent(this, StudentList.class);
        startActivity(intent);
    }

    public void onTeacherClicked(View view) {
        Intent intent = new Intent(this, TeacherList.class);
        startActivity(intent);
    }
    public void onInboxClicked(View view) {
        Intent intent = new Intent(this, Messages.class);
        startActivity(intent);
    }
}
