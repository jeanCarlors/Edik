package com.sinapse.direction.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.sinapse.direction.R;

import java.time.Instant;
import java.time.LocalDate;

public class ActivityList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }
    public void onDetailsClicked(View view) {
        Intent intent = new Intent(this, ActivityDetails.class);
        startActivity(intent);
    }

    public void onDateBtnClicked(View view) {
        DatePickerDialog picker = new DatePickerDialog(ActivityList.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                       // eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, 2020, 05, 13);
        picker.show();
    }
}
