package com.sinapse.professeur.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sinapse.professeur.R;
import com.sinapse.professeur.ui.helper.CourseProcessingAdapter;
import com.sinapse.professeur.ui.helper.StudentsByClassroomAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

public class CourseProcessing extends AppCompatActivity {
    private RecyclerView courseProcessingRecyclerView;
    private CourseProcessingAdapter courseProcessingAdapter;
    private ArrayList<String> courseProcessingList = new ArrayList<>();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> echanges;
    Date date;

    int PICKFILE_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_processing);
        String chapter = getIntent().getExtras().getString("chapter");
        String title = getIntent().getExtras().getString("title");
        String subject = getIntent().getExtras().getString("subject");
        final String courseHeader = getIntent().getExtras().getString("courseHeader");

        TextView chapterTv = findViewById(R.id.chapter);
        TextView subjectTv = findViewById(R.id.subject);
        TextView titleTv = findViewById(R.id.title);
        final EditText contentEdt = findViewById(R.id.edt_content);
        Button sendBtn = findViewById(R.id.send_btn);
        Button mediaBtn = findViewById(R.id.media_btn);

        subjectTv.setText(subject);
        chapterTv.setText(chapter);
        titleTv.setText(title);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                echanges = new HashMap<>();
                date = new Date();
                echanges.put(date.toString() + "%" + "Professeur"
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

        /*db.collection("00000001").document("ac_2019_2020").collection("Classes")
                .document("NS I A").collection("Courses").document(courseHeader)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();

                        Iterator iter = map.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            if(!(entry.getKey().equals("titre") || entry.getKey().equals("Matiere")
                                    || entry.getKey().equals("chapitre"))) {
                                courseProcessingList.add(entry.getKey() + " : " + entry.getValue());
                            }
                        }
                    }
                }
            }
        }).addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("testData", String.valueOf(courseProcessingList.size()));
                        courseProcessingAdapter = new CourseProcessingAdapter(getApplicationContext(),
                                courseProcessingList);
                        courseProcessingRecyclerView = findViewById(R.id.course_recycler_view);
                        courseProcessingRecyclerView.setAdapter(courseProcessingAdapter);
                        courseProcessingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                });*/

        mediaBtn.setOnClickListener(new View.OnClickListener() {
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
                    echanges.put(date.toString() + "%" + "Professeur"
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
