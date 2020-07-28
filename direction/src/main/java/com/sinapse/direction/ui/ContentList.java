package com.sinapse.direction.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.sinapse.direction.R;
import com.sinapse.direction.ui.helper.SubjectAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContentList extends AppCompatActivity {
    private RecyclerView subjectRecyclerView;
    private SubjectAdapter subjectAdapter;
    private List<String> subjectList = new ArrayList<>();

    private ProgressDialog progressDialog;

    private Intent intent;
    private Bundle bundle;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private StorageReference rootContentSubject = storage.getReference().child("/Edik Content");
    private String rootUrl = "gs://edik-6adf5.appspot.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Chargement de la page...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        intent = getIntent();
        bundle = intent.getExtras();
        openContentSubject(bundle.getString("grade"), progressDialog);
        TextView subjectTextView = findViewById(R.id.header_subject_text_view);
        subjectTextView.setText("LES CONTENUS DE SINAPSE DU " + bundle.getString("grade").substring(1));
    }

    public void onMathBtnClicked(View view) {
        Intent intent = new Intent(this, ContentByChapter.class);
        startActivity(intent);
    }

    public void openContentSubject(String grade, final ProgressDialog progressDialog){
        rootContentSubject.child(grade).listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        Log.d("test", String.valueOf(listResult.getPrefixes().size()));
                        for (StorageReference item : listResult.getPrefixes()) {
                            subjectList.add(item.getName());
                        }
                        subjectRecyclerView = findViewById(R.id.subject_recycler_view);
                        progressDialog.dismiss();
                        subjectAdapter = new SubjectAdapter(getApplicationContext(), (ArrayList<String>) subjectList);
                        subjectRecyclerView.setAdapter(subjectAdapter);
                        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                      Log.d("Root content subject", "Connexion failed"); // Uh-oh, an error occurred!
                    }
                });
    }
}
