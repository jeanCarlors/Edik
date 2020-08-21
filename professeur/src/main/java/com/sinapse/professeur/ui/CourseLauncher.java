package com.sinapse.professeur.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sinapse.professeur.R;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CourseLauncher extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_launcher);

        final EditText chapter = findViewById(R.id.edt_course_chapter);
        final EditText title = findViewById(R.id.edt_course_title);
        final EditText subject = findViewById(R.id.edt_course_subject);

        Button course = findViewById(R.id.course_launcher_btn);

        //final String courseHeader = Instant.now().toString() + "%" + subject.getText().toString();
        Timestamp timestamp = Timestamp.now();
        Log.d("timestampTest", timestamp.toString());
        //LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("PRT"));
        //Date date = new Date();
        //Log.d("localeDate", localDateTime.toString());


        course.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Date date = new Date();
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                String dateCourse = DATE_FORMAT.format(date);

                final String courseHeader = date.toString() + "%" + subject.getText().toString();
                Log.d("LocalDateTime", courseHeader.split("%")[0]);
                //Timestamp timestamp = getTimestamp(courseHeader.split("%")[0]);
                //Log.d("timestampTest", timestamp.toString());
                Intent intent = new Intent(getApplicationContext(), CourseProcessing.class);

                Map<String, Object> echanges = new HashMap<>();
                Map<String, Object> header = new HashMap<>();
                header.put("chapitre", chapter.getText().toString());
                header.put("titre", title.getText().toString());
                header.put("Matiere", subject.getText().toString());
                echanges.put(dateCourse+ "%" +"Salutation","Bonjour");

                db.collection("00000001").document("ac_2019_2020")
                        .collection("Classes").document("NS I A")
                        .collection("Courses").document(courseHeader).set(header);

                intent.putExtra("chapter", chapter.getText().toString());
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("subject", subject.getText().toString());
                intent.putExtra("courseHeader", courseHeader);
                startActivity(intent);
            }
        });
    }
}
