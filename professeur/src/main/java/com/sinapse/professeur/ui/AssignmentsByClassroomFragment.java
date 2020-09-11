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
import com.sinapse.professeur.ui.helper.AssignmentAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssignmentsByClassroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignmentsByClassroomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> assignments = new ArrayList<>();
    private RecyclerView assignmentRecyclerView;
    //private ProgressDialog progressDialog;
    private ArrayList<String> assignmentList = new ArrayList<>();

    public AssignmentsByClassroomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignmentsByClassroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignmentsByClassroomFragment newInstance(String param1, String param2) {
        AssignmentsByClassroomFragment fragment = new AssignmentsByClassroomFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_assignments_by_classroom, container, false);
        Button courseLauncher = rootView.findViewById(R.id.assignment_launcher);
        final String classroom = getActivity().getIntent().getExtras().getString("classroom");
        courseLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AssignmentsForSubmitting.class);
                intent.putExtra("classroom", classroom);
                getActivity().startActivity(intent);
            }
        });

        db.collection("00000001").document("ac_2019_2020")
                .collection("Classes").document(classroom)
                .collection("Assignments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                assignments.add(document.getId());
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
                        final int chatTest = Log.d("chatTest", String.valueOf(assignments.size()));

                        for(String assignment: assignments) {
                            assignmentList.add(assignment);
                        }

                        AssignmentAdapter assignmentAdapter = new AssignmentAdapter(getContext(), (ArrayList<String>) assignmentList);
                        assignmentRecyclerView = rootView.findViewById(R.id.assignments_recycler_view);
                        assignmentRecyclerView.setAdapter(assignmentAdapter);
                        assignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        assignmentAdapter.notifyDataSetChanged();
                    }
                }
        );

        return rootView;
    }
}