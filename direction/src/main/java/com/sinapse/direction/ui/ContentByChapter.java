package com.sinapse.direction.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.sinapse.direction.ui.helper.ChapterAdapter;
import com.sinapse.direction.ui.helper.SubjectAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContentByChapter extends AppCompatActivity {
    private RecyclerView chapterRecyclerView;
    private ChapterAdapter chapterAdapter;
    private List<String> chapterList = new ArrayList<>();

    Intent intent;
    Bundle bundle;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private StorageReference rootContentSubject = storage.getReference().child("/Edik Content");
    private String rootUrl = "gs://edik-6adf5.appspot.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_by_chapter);
        intent = getIntent();
        bundle = intent.getExtras();
        TextView chapterTextView = findViewById(R.id.header_chapter_text_view);
        chapterTextView.setText("LES CONTENUS DE SINAPSE DU " + bundle.getString("subject")
                .replaceFirst("/", " ").replace("/", "-"));

        openContentChapter(bundle.getString("subject"));
    }

    public void onChapterClicked(View view) {
        Intent intent = new Intent(this, ContentDetails.class);
        startActivity(intent);
    }

    public void openContentChapter(String chapter){
        rootContentSubject.child(chapter).listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        Log.d("test", String.valueOf(listResult.getPrefixes().size()));
                        for (StorageReference item : listResult.getPrefixes()) {
                            chapterList.add(item.getName());
                        }
                        chapterRecyclerView = findViewById(R.id.chapter_recycler_view);
                        chapterAdapter = new ChapterAdapter(getApplicationContext(), (ArrayList<String>) chapterList);
                        chapterRecyclerView.setAdapter(chapterAdapter);
                        chapterRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
