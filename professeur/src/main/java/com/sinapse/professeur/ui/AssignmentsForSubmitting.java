package com.sinapse.professeur.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sinapse.professeur.R;
import com.sinapse.professeur.ui.helper.AssignmentAdapter;
import com.sinapse.professeur.ui.helper.CourseLauncherAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AssignmentsForSubmitting extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_for_submitting);
        final EditText subject = findViewById(R.id.edit_text_subject);
        final EditText title = findViewById(R.id.edit_text_title);
        final EditText libelle = findViewById(R.id.edit_text_libelle);
        final EditText dateDeRemise = findViewById(R.id.edit_text_date);
        Button sendBtn = findViewById(R.id.send_btn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                //SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                //String dateCourse = DATE_FORMAT.format(date);

                final String courseHeader = date.toString() + "%" + subject.getText().toString();
                Map<String, Object> assignment = new HashMap<>();
                assignment.put("libelle", libelle.getText().toString());
                assignment.put("titre", title.getText().toString());
                assignment.put("Matiere", subject.getText().toString());
                assignment.put("dateEmission", date.toString());
                assignment.put("dateDeRemise", dateDeRemise.getText().toString());

                db.collection("00000001").document("ac_2019_2020")
                        .collection("Classes").document("NS I A")
                        .collection("Assignments").document(courseHeader).set(assignment);
                libelle.setText("");
                title.setText("");
                subject.setText("");
                dateDeRemise.setText("");
            }
        });
    }

}
