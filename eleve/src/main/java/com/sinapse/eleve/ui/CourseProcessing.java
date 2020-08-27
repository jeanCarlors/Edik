package com.sinapse.eleve.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sinapse.eleve.R;
import com.sinapse.eleve.ui.helper.CourseProcessingAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CourseProcessing extends AppCompatActivity {
    private RecyclerView courseProcessingRecyclerView;
    private CourseProcessingAdapter courseProcessingAdapter;
    private ArrayList<String> courseProcessingList = new ArrayList<>();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    Map<String, Object> echanges;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int PICKFILE_REQUEST_CODE = 0;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_processing);
        final String courseHeader = getIntent().getExtras().getString("courseHeader");
        final EditText contentEdt = findViewById(R.id.edt_content);
        Button sendBtn = findViewById(R.id.send_btn);
        Button mediaBtn = findViewById(R.id.media_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                echanges = new HashMap<>();
                Date date = new Date();
                echanges.put(date.toString() + "%" + "Eleve"
                        , contentEdt.getText().toString());
                db.collection("00000001").document("ac_2019_2020")
                        .collection("Classes").document("NS I A")
                        .collection("Courses").document(courseHeader)
                        .update(echanges);
                contentEdt.setText("");

                db.collection("00000001").document("ac_2019_2020").collection("Classes")
                        .document("NS I A").collection("Courses")
                        .document(courseHeader).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (snapshot != null && snapshot.exists()) {
                            Map<String, Object> map = snapshot.getData();
                            courseProcessingList.clear();
                            Iterator iter = map.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                if(!(entry.getKey().equals("titre") || entry.getKey().equals("Matiere")
                                        || entry.getKey().equals("chapitre"))) {
                                    courseProcessingList.add(entry.getKey() + " > " + entry.getValue());
                                }
                            }
                            Collections.sort(courseProcessingList);
                            Log.d("testData", String.valueOf(courseProcessingList.size()));
                            courseProcessingAdapter = new CourseProcessingAdapter(getApplicationContext(),
                                    courseProcessingList);
                            courseProcessingRecyclerView = findViewById(R.id.course_recycler_view);
                            courseProcessingRecyclerView.setAdapter(courseProcessingAdapter);
                            courseProcessingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            courseProcessingAdapter.notifyDataSetChanged();
                            courseProcessingRecyclerView.smoothScrollToPosition(courseProcessingAdapter.getItemCount());
                        }
                    }
                });
            }

        });

        final Button appelBtn = findViewById(R.id.presence_btn);
        appelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)findViewById(R.id.appel_layout)).setVisibility(View.INVISIBLE);
                Toast.makeText(v.getContext(), "Vous avez répondu à l'appel", Toast.LENGTH_LONG).show();
            }
        });

        mediaBtn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            PICKFILE_REQUEST_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK){
            date = new Date();
            final String courseHeader = getIntent().getExtras().getString("courseHeader");
            Uri content_describer = data.getData();
            String mimeType = getContentResolver().getType(content_describer);
            String path = "School Management/00000001/ac_2019_2020/Classes/NS I A/Courses/"
                    +courseHeader+"/Muiltimedia - "+date.toString()+"/"
                    +mimeType.split("/")[0]+"."+mimeType.split("/")[1];
            final StorageReference riversRef = storageRef.child(path);
            UploadTask uploadTask = riversRef.putFile(content_describer);
            //Toast.makeText(this, "################# "+mimeType, Toast.LENGTH_LONG).show();
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    echanges = new HashMap<>();
                    date = new Date();
                    echanges.put(date.toString() + "%" + "Eleve"
                            , riversRef.getPath());
                    //riversRef.getDownloadUrl().getResult().toString();
                    db.collection("00000001").document("ac_2019_2020")
                            .collection("Classes").document("NS I A")
                            .collection("Courses").document(courseHeader)
                            .update(echanges);
                    Log.d("ENFIN", "Enfin - " + taskSnapshot.getStorage().toString());
                }
            });

        }
    }
}