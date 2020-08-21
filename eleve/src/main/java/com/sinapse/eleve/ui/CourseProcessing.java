package com.sinapse.eleve.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sinapse.eleve.R;
import com.sinapse.eleve.ui.helper.CourseProcessingAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CourseProcessing extends AppCompatActivity {
    private RecyclerView courseProcessingRecyclerView;
    private CourseProcessingAdapter courseProcessingAdapter;
    private ArrayList<String> courseProcessingList = new ArrayList<>();
    Map<String, Object> echanges;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_processing);
        final String courseHeader = getIntent().getExtras().getString("courseHeader");
        final EditText contentEdt = findViewById(R.id.edt_content);
        Button sendBtn = findViewById(R.id.send_btn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                echanges = new HashMap<>();
                echanges.put(Timestamp.now().toString() + "%" + "Eleve"
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

        Toast.makeText(this, courseHeader, Toast.LENGTH_LONG).show();
    }
}