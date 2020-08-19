package com.sinapse.direction.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.sinapse.direction.R;
import com.sinapse.direction.databinding.ActivityContentDetailsBinding;
import com.sinapse.direction.ui.Models.Topic;
import com.sinapse.direction.ui.helper.ContentTopicAdapter;
import com.sinapse.direction.ui.helper.ContentViewer;
import com.sinapse.direction.ui.helper.TopicAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContentDetails extends AppCompatActivity {
    private RecyclerView topicRecyclerView;
    private ContentTopicAdapter topicAdapter;
    private List<Topic> topicList = new ArrayList<>();

    private ProgressDialog progressDialog;

    private Intent intent;
    private Bundle bundle;

    private static final int PICK_PDF_FILE = 2;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    StorageReference rootContentTopic = storage.getReference().child("/Edik Content");
    private String rootUrl = "gs://edik-6adf5.appspot.com";

    ActivityContentDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); //R.layout.activity_content_details
        setSupportActionBar(binding.toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Chargement de la page...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        intent = getIntent();
        bundle = intent.getExtras();
        TextView topicTextView = findViewById(R.id.header_topic_text_view);
        String root = "/";
        String pathTopic = "/" + (bundle.getString("chapter").replaceFirst("-", "/"));
        Log.d("test4",pathTopic);
        topicTextView.setText(pathTopic.substring(1).replaceAll("/", " / "));

        setTitle(bundle.getString("title"));

        openContentTopic(pathTopic, progressDialog);
    }

    public void openContentTopic(final String topic, final ProgressDialog progressDialog){
        rootContentTopic.child(topic).listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        topicRecyclerView = findViewById(R.id.topic_recycler_view);
                        progressDialog.dismiss();
                        topicAdapter = new ContentTopicAdapter(getApplicationContext(), listResult.getPrefixes());
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
