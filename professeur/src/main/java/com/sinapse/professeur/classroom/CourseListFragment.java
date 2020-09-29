package com.sinapse.professeur.classroom;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sinapse.libmodule.beans.Classroom;
import com.sinapse.libmodule.beans.Cours;
import com.sinapse.libmodule.beans.Course;
import com.sinapse.libmodule.beans.Session;
import com.sinapse.professeur.R;
import com.sinapse.professeur.course.CourseDetailsActivity;
import com.sinapse.professeur.databinding.FragmentCourseListBinding;
import com.sinapse.professeur.viewholders.ClassViewHolder;

public class CourseListFragment extends Fragment {

    FragmentCourseListBinding binding;
    static final String KEY_PARAM = "CLASS_PATH";
    String path;
    FirestoreRecyclerAdapter<Course, ClassViewHolder> adapter;

    public CourseListFragment() {
        // Required empty public constructor
    }

    public static CourseListFragment newInstance(String param_path) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_PARAM, param_path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            path = getArguments().getString(KEY_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCourseListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Query query = FirebaseFirestore.getInstance()
                .document(path)
                .collection("cours")
                .whereEqualTo("prof", Session.currentUser.getUid());

        FirestoreRecyclerOptions<Course> courses = new FirestoreRecyclerOptions.Builder<Course>()
                .setQuery(query, new SnapshotParser<Course>() {
                    @NonNull
                    @Override
                    public Course parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Course course = new Course();
                        course.setSubject(snapshot.getString("name"));
                        course.setTheme(snapshot.getString("description"));
                        course.setDocPath(snapshot.getReference().getPath());

                        return course;
                    }
                }).build();

        adapter = new FirestoreRecyclerAdapter<Course, ClassViewHolder>(courses) {
            @Override
            protected void onBindViewHolder(@NonNull ClassViewHolder holder, int position, @NonNull final Course model) {
                holder.title.setText(model.getSubject());
                holder.subTitle.setText(model.getTheme());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
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

        binding.recycleViewCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleViewCourses.setAdapter(adapter);

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