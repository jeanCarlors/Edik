package com.sinapse.professeur.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.sinapse.professeur.R;
import com.sinapse.professeur.ui.helper.TopicAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContentDetails extends AppCompatActivity {
    private RecyclerView topicRecyclerView;
    private TopicAdapter topicAdapter;
    private List<String> topicList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Intent intent;
    private Bundle bundle;

    private static final int PICK_PDF_FILE = 2;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    StorageReference rootContentTopic = storage.getReference().child("/Edik Content");
    private String rootUrl = "gs://edik-6adf5.appspot.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_details);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Chargement de la page ...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        intent = getIntent();
        bundle = intent.getExtras();
        TextView topicTextView = findViewById(R.id.header_topic_text_view);
        String root = "/";
        String pathTopic = "/" + (bundle.getString("chapter").replaceFirst("-", "/"));
        Log.d("test4",pathTopic);
        topicTextView.setText("LES CONTENUS DE SINAPSE DU " + bundle.getString("chapter"));
        openContentTopic(pathTopic);
    }

    public void openContentTopic(final String topic){
        rootContentTopic.child(topic).listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        Log.d("topic", String.valueOf(listResult.getPrefixes().size()));
                        for (StorageReference item : listResult.getPrefixes()) {
                            topicList.add(item.getName());
                        }
                        topicRecyclerView = findViewById(R.id.topic_recycler_view);
                        progressDialog.dismiss();
                        topicAdapter = new TopicAdapter(getApplicationContext(), (ArrayList<String>) topicList);
                        topicRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        //topicRecyclerView.setHasFixedSize(true);
                        topicRecyclerView.setAdapter(topicAdapter);
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
