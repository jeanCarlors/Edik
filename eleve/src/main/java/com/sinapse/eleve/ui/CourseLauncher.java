package com.sinapse.eleve.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sinapse.eleve.R;
import com.sinapse.eleve.ui.helper.CourseLauncherAdapter;
import com.sinapse.eleve.ui.helper.TeachersByClassroomAdapter;

//import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;

public class CourseLauncher extends AppCompatActivity {

    private RecyclerView courseLauncherRecyclerView;
    private CourseLauncherAdapter courseLauncherAdapter;
    private ArrayList<String> courses = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private ArrayList<String> courseList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_launcher);

        String classroom = getIntent().getExtras().getString("classroom");
        //Log.d("classroomForTeach", classroom);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Chargement de la page...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        //progressDialog.show();

        //LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("PRT"));
        //Log.d("localeDate", localDateTime.toString());

        db.collection("00000001").document("ac_2019_2020")
                .collection("Classes").document(classroom)
                .collection("Courses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                courses.add(document.getId());
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                }).addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>(){

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        final int chatTest = Log.d("chatTest", String.valueOf(courses.size()));

                        for(String course: courses) {
                            courseList.add(course);
                        }

                        CourseLauncherAdapter courseLauncherAdapter = new CourseLauncherAdapter(getApplicationContext(), (ArrayList<String>) courseList);
                        courseLauncherRecyclerView = findViewById(R.id.courses_recycler_view);
                        courseLauncherRecyclerView.setAdapter(courseLauncherAdapter);
                        courseLauncherRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        courseLauncherAdapter.notifyDataSetChanged();
                    }
                }
        );
    }
}