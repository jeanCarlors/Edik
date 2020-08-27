package com.sinapse.professeur.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sinapse.professeur.R;
import com.sinapse.professeur.ui.helper.CourseLauncherAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesByClassroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesByClassroomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> courses = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView courseLauncherRecyclerView;
    //private ProgressDialog progressDialog;
    private ArrayList<String> courseList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CoursesByClassroomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesByClassroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesByClassroomFragment newInstance(String param1, String param2) {
        CoursesByClassroomFragment fragment = new CoursesByClassroomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_courses_by_classroom, container, false);
        Button courseLauncher = rootView.findViewById(R.id.course_launcher);
        String classroom = getActivity().getIntent().getExtras().getString("classroom");
        courseLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CourseLauncher.class);
                getActivity().startActivity(intent);
            }
        });

        db.collection("00000001").document("ac_2019_2020")
                .collection("Classes").document(classroom)
                .collection("Courses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                courses.add(document.getId());
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                }).addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>(){

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        final int chatTest = Log.d("chatTest", String.valueOf(courses.size()));

                        for(String course: courses) {
                            courseList.add(course);
                        }

                        CourseLauncherAdapter courseLauncherAdapter = new CourseLauncherAdapter(getContext(), (ArrayList<String>) courseList);
                        courseLauncherRecyclerView = rootView.findViewById(R.id.course_recycler_view);
                        courseLauncherRecyclerView.setAdapter(courseLauncherAdapter);
                        courseLauncherRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        courseLauncherAdapter.notifyDataSetChanged();
                    }
                }
        );

        return rootView;
    }
}