package com.sinapse.direction.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sinapse.direction.R;
import com.sinapse.direction.ui.helper.GradeListAdapter;
import com.sinapse.direction.ui.helper.StudentsByClassroomAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentsByClassroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentsByClassroomFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView studentsByClassroomRecyclerView;
    private StudentsByClassroomAdapter studentsByClassroomAdapter;
    private ArrayList<String> studentList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentsByClassroomFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentsByClassroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentsByClassroomFragment newInstance(String param1, String param2) {
        StudentsByClassroomFragment fragment = new StudentsByClassroomFragment();
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
        final View rootView =  inflater.inflate(R.layout.fragment_students_by_classroom, container, false);
        String classroom = getActivity().getIntent().getExtras().getString("classroom");
        //getActivity().getIntent().getExtras().getString("")
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Chargement de la page...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        db.collection("00000001").document("ac_2019_2020").collection("Classes")
                .document(classroom).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        final ArrayList students = (ArrayList) document.getData().get("eleves");
                        if(students != null && students.size() > 0){
                        for (DocumentReference studentRefrence : (ArrayList<DocumentReference>) students) {
                            studentRefrence.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String studentName = document.getData().get("nom").toString() + " " + document.getData().get("prenom").toString();
                                            Log.d("student", studentName);
                                            studentList.add(studentName);
                                            Log.d("studentSize", String.valueOf(studentList.size()));
                                        }
                                    }
                                }
                            }).addOnSuccessListener(
                                    new OnSuccessListener<DocumentSnapshot>() {

                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (studentList.size() == students.size() && studentList.size() != 0) {
                                                progressDialog.dismiss();
                                                Log.d("studentSize", String.valueOf(studentList.size()));
                                                studentsByClassroomAdapter = new StudentsByClassroomAdapter(getContext(), (ArrayList<String>) studentList);
                                                studentsByClassroomRecyclerView = rootView.findViewById(R.id.student_list_by_classroom_recycler_view);
                                                studentsByClassroomRecyclerView.setAdapter(studentsByClassroomAdapter);
                                                studentsByClassroomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                studentsByClassroomAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                            );
                        }
                    }else{
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Il n'y a aucun eleve enregistre dans cette classe", Toast.LENGTH_LONG)
                            .show();
                        }
                    }
                }
            }
        });
        return rootView;
    }

}