package com.sinapse.professeur.course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sinapse.libmodule.beans.CourseSession;
import com.sinapse.libmodule.beans.CourseSessionMessage;
import com.sinapse.professeur.R;
import com.sinapse.professeur.databinding.ActivityCourseSessionBinding;
import com.sinapse.professeur.viewholders.ClassViewHolder;
import com.sinapse.professeur.viewholders.CourseSessionMessageViewHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CourseSessionActivity extends AppCompatActivity {

    ActivityCourseSessionBinding binding;
    SimpleDateFormat dtFormat;

    boolean _details = false;
    boolean _list = false;

    FirestoreRecyclerAdapter<CourseSessionMessage, CourseSessionMessageViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseSessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//R.layout.activity_course_session
        setSupportActionBar(binding.toolbar);

        final String path = getIntent().getStringExtra("PATH");
        if (path == null) {
            setResult(RESULT_CANCELED);
            finish();
        }

        setTitle("Session");
        dtFormat = new SimpleDateFormat("EEE, dd MMM, hh:mm", Locale.FRANCE);

        FirebaseFirestore.getInstance()
                .document(path)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        binding.sessionDate.setText(dtFormat.format(doc.getTimestamp("created_date").toDate()));
                        String status = doc.getString("status");
                        binding.sessionStatus.setText(status);
                        binding.sessionStatus.setTextColor(
                                "EN COURS".equals(status)
                                        ? Color.GREEN
                                        : Color.RED);

                        _details = true;
                        updateProgressBar();
                    }
                });

        Query query = FirebaseFirestore.getInstance()
                .document(path)
                .collection("messages");

        FirestoreRecyclerOptions<CourseSessionMessage> recyclerOptions = new FirestoreRecyclerOptions.Builder<CourseSessionMessage>()
                .setQuery(query, new SnapshotParser<CourseSessionMessage>() {
                    @NonNull
                    @Override
                    public CourseSessionMessage parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        CourseSessionMessage msg = new CourseSessionMessage();
                        msg.setUid(snapshot.getId());
                        msg.setDocPath(snapshot.getReference().getPath());
                        msg.setFromUID(snapshot.getString("from_uid"));
                        msg.setFromName(snapshot.getString("from_name"));
                        msg.setType(snapshot.getString("type"));
                        msg.setContent(snapshot.getString("message"));
                        msg.setCreated_date(snapshot.getTimestamp("created_date").toDate());
                        return msg;
                    }
                })
                .build();

        adapter = new FirestoreRecyclerAdapter<CourseSessionMessage, CourseSessionMessageViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull CourseSessionMessageViewHolder holder, int position, @NonNull CourseSessionMessage model) {
                holder.tvFrom.setText(model.getFromName());
                holder.tvDate.setText(dtFormat.format(model.getCreated_date()));
                holder.tvContent.setText(model.getContent());

                holder.imgType.setVisibility(View.GONE);
                holder.imgDownload.setVisibility(View.GONE);

                _list = true;
                updateProgressBar();
            }

            @NonNull
            @Override
            public CourseSessionMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.course_session_message_view_item, parent, false);
                return new CourseSessionMessageViewHolder(view);
            }
        };

        binding.recycleViewCourseSession.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleViewCourseSession.setAdapter(adapter);

    }

    private void updateProgressBar() {
        binding.progressCircular.setVisibility(
                (_details && _list)
                        ? View.GONE : View.VISIBLE
        );
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}