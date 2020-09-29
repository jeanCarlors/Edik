package com.sinapse.professeur.course;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sinapse.libmodule.beans.CourseSession;
import com.sinapse.professeur.R;
import com.sinapse.professeur.databinding.FragmentCourseSessionsBinding;
import com.sinapse.professeur.viewholders.ClassViewHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseSessionsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseSessionsListFragment extends Fragment {


    FragmentCourseSessionsBinding binding;
    private static final String ARG_PARAM1 = "COURSE_PATH";
    private String course_path;
    FirestoreRecyclerAdapter<CourseSession, ClassViewHolder> adapter;

    public CourseSessionsListFragment() {
        // Required empty public constructor
    }

    public static CourseSessionsListFragment newInstance(String param1) {
        CourseSessionsListFragment fragment = new CourseSessionsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            course_path = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCourseSessionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Query query = FirebaseFirestore.getInstance()
                .document(course_path)
                .collection("sessions");

        FirestoreRecyclerOptions<CourseSession> recyclerOptions = new FirestoreRecyclerOptions.Builder<CourseSession>()
                .setQuery(query, new SnapshotParser<CourseSession>() {
                    @NonNull
                    @Override
                    public CourseSession parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        CourseSession session = new CourseSession();
                        session.setUid(snapshot.getId());
                        session.setDocPath(snapshot.getReference().getPath());
                        session.setStatus(snapshot.getString("status"));
                        session.setCreated_date(snapshot.getTimestamp("created_date").toDate());
                        session.setEnded_date(snapshot.getTimestamp("ended_date").toDate());
                        return session;
                    }
                }).build();

        adapter = new FirestoreRecyclerAdapter<CourseSession, ClassViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ClassViewHolder holder, int position, @NonNull final CourseSession model) {

                SimpleDateFormat dtFormat = new SimpleDateFormat("EEEE, dd MMM", Locale.FRANCE);

                holder.title.setText(String.format(
                        Locale.FRANCE,
                        "Session du %s",
                        dtFormat.format(model.getCreated_date())
                ));
                holder.subTitle.setText(model.getStatus());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), CourseSessionActivity.class);
                        intent.putExtra("PATH", model.getDocPath());
                        startActivity(intent);
                    }
                });


                binding.progressCircular.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_2, parent, false);
                return new ClassViewHolder(view);
            }
        };

        binding.recycleViewCourseSessions.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleViewCourseSessions.setAdapter(adapter);

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