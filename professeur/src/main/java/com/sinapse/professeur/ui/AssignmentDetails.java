package com.sinapse.professeur.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sinapse.professeur.R;

public class AssignmentDetails extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_details);
        String assignmentHeader = getIntent().getExtras().getString("assignmentHeader");
        final TextView subject = findViewById(R.id.textview_subject);
        final TextView title = findViewById(R.id.textview_title);
        final TextView libelle = findViewById(R.id.textview_libelle);
        final TextView dateEmission = findViewById(R.id.textview_date_emission);
        final TextView dateDeRemise = findViewById(R.id.textview_date_remise);


        db.collection("00000001").document("ac_2019_2020")
                .collection("Classes").document("NS I A")
                .collection("Assignments")
                .document(assignmentHeader).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                subject.setText(document.getData().get("Matiere").toString());
                                title.setText(document.getData().get("titre").toString());
                                libelle.setText(document.getData().get("libelle").toString());
                                dateEmission.setText(document.getData().get("dateEmission").toString());
                                dateDeRemise.setText(document.getData().get("dateDeRemise").toString());
                                } else {
                                Log.d("TAG", "No such document");
                            }
                        }
                    }
                });
    }
}