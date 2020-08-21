package com.sinapse.direction.ui;

import android.app.ProgressDialog;
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
import com.sinapse.direction.R;
import com.sinapse.direction.ui.helper.StudentsByClassroomAdapter;
import com.sinapse.direction.ui.helper.TeachersByClassroomAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeachersByClassroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeachersByClassroomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView teachersByClassroomRecyclerView;
    private TeachersByClassroomAdapter teachersByClassroomAdapter;
    private ArrayList<String> teacherList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeachersByClassroomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeachersByClassroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeachersByClassroomFragment newInstance(String param1, String param2) {
        TeachersByClassroomFragment fragment = new TeachersByClassroomFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_teachers_by_classroom, container, false);

        String classroom = getActivity().getIntent().getExtras().getString("classroom");

        //getActivity().getIntent().getExtras().getString("")
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Chargement de la page...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        //progressDialog.show();
        db.collection("00000001").document("ac_2019_2020").collection("Classes")
                .document(classroom).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        final ArrayList teachers = (ArrayList) document.getData().get("professeurs");
                        if(teachers != null){
                            for (DocumentReference teacherReference : (ArrayList<DocumentReference>) teachers) {
                                teacherReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String teacherName = document.getData().get("nom").toString();
                                                Log.d("teacher", teacherName);
                                                teacherList.add(teacherName);
                                                Log.d("studentSize", String.valueOf(teacherList.size()));
                                            }
                                        }
                                    }
                                }).addOnSuccessListener(
                                        new OnSuccessListener<DocumentSnapshot>() {

                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (teacherList.size() == teachers.size() && teacherList.size() != 0) {
                                                    progressDialog.dismiss();
                                                    Log.d("studentSize", String.valueOf(teacherList.size()));
                                                    teachersByClassroomAdapter = new TeachersByClassroomAdapter(getContext(), (ArrayList<String>) teacherList);
                                                    teachersByClassroomRecyclerView = rootView.findViewById(R.id.teacher_list_by_classroom_recycler_view);
                                                    teachersByClassroomRecyclerView.setAdapter(teachersByClassroomAdapter);
                                                    teachersByClassroomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                    teachersByClassroomAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                );
                            }
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Il n'y a aucun eleve professeur dans cette classe", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        });
        //studentsByClassroomAdapter.notifyDataSetChanged();
        Log.d("student", "student");

        return rootView;
    }
}