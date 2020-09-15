package com.sinapse.eleve.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sinapse.eleve.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileUpdate extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> gradeList = new ArrayList<>();
    Spinner gradeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        final Button schoolProfileBtn = findViewById(R.id.profile_update_btn);
        schoolProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileBtnClicked(v);
            }
        });
    }

    public void onProfileBtnClicked(View view) {
        final String username = getIntent().getExtras().getString("username");
        //String grade = getIntent().getExtras().getString("grade");
        //String school = getIntent().getExtras().getString("school");
        EditText edt_name = (EditText) view.getRootView().findViewById(R.id.edit_text_student_name);
        EditText edt_firstname = (EditText) view.getRootView().findViewById(R.id.edit_text_student_firstname);
        EditText edt_telephone = (EditText) view.getRootView().findViewById(R.id.edit_text_student_telephone);
        EditText edt_responsable_name = (EditText) view.getRootView().findViewById(R.id.edit_text_responsable_name);
        EditText edt_responsable_tel = (EditText) view.getRootView().findViewById(R.id.edit_text_responsable_telephone);
        final DocumentReference studentDocumentReferenceForRemoving = db.collection("00000001")
                .document("ac_2019_2020").collection("Eleves")
                .document(username);

        final Map<String, Object> profil = new HashMap<>();
        if (edt_name.getText().toString().length() > 0)
            profil.put("nom", edt_name.getText().toString());

        if (edt_firstname.getText().toString().length() > 0)
            profil.put("prenom", edt_firstname.getText().toString());

        if (edt_telephone.getText().toString().length() > 0)
            profil.put("telephone", edt_telephone.getText().toString());

        if (edt_responsable_name.getText().toString().length() > 0)
            profil.put("responsable", edt_responsable_name.getText().toString());
        if (edt_responsable_tel.getText().toString().length() > 0)
            profil.put("telephone responsable", edt_responsable_tel.getText().toString());

        if (gradeSpinner.getSelectedItem().toString().length() > 0) {
            DocumentReference classeDocumentreference = db.collection("00000001")
                    .document("ac_2019_2020").collection("Classes")
                    .document(gradeSpinner.getSelectedItem().toString());
            profil.put("classe", classeDocumentreference);
        }

        if (edt_name.getText().toString().length() > 0 || edt_firstname.getText().toString().length() > 0
                || edt_telephone.getText().toString().length() > 0 || edt_responsable_name.getText().toString().length() > 0
                || edt_responsable_tel.getText().toString().length() > 0){
            db.collection("00000001").document("ac_2019_2020")
                    .collection("Eleves")
                    .document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if(!document.getData().get("classe").equals(db.collection("00000001")
                                    .document("ac_2019_2020").collection("Classes")
                                    .document(gradeSpinner.getSelectedItem().toString()))){
                                DocumentReference studentClassroom = (DocumentReference)document.getData().get("classe");
                                studentClassroom.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        String classroom = document.getId();
                                                        db.collection("00000001").document("ac_2019_2020").collection("Classes")
                                                                .document(classroom).update("eleves",
                                                                FieldValue.arrayRemove(studentDocumentReferenceForRemoving));

                                                    }else{
                                                        db.collection("00000001").document("ac_2019_2020")
                                                                .collection("Professeurs")
                                                                .document(username).set(profil);
                                                    }
                                                }
                                            }
                                        });
                            }
                            db.collection("00000001").document("ac_2019_2020")
                                    .collection("Eleves")
                                    .document(username).update(profil);
                        } else {
                            db.collection("00000001").document("ac_2019_2020")
                                    .collection("Eleves")
                                    .document(username).set(profil);
                        }
                    }
                }
            });
            edt_name.setText("");
            edt_firstname.setText("");
            edt_telephone.setText("");
            edt_responsable_name.setText("");
            edt_responsable_tel.setText("");
            edt_name.setHint("Rentrez votre nom ");
            edt_firstname.setHint("Rentrez votre prenom ");
            edt_telephone.setHint("Rentrez votre téléphone ");
            edt_responsable_name.setHint("Le nom de votre responsable");
            edt_responsable_tel.setHint("Téléphone responsable");
        }

        DocumentReference studentDocumentReference = db.collection("00000001")
                .document("ac_2019_2020").collection("Eleves")
                .document(username);

        db.collection("00000001").document("ac_2019_2020").collection("Classes")
                .document(gradeSpinner.getSelectedItem().toString()).update("eleves", FieldValue.arrayUnion(studentDocumentReference));


        //edt_grade.setHint("Rentrez votre classe"); */
        //Toast.makeText(getContext(), spinner_grade.getSelectedItem().toString(), LENGTH_LONG).show();
        Toast.makeText(this, "Le profil est enregistré", Toast.LENGTH_LONG).show();
    }
}