package com.sinapse.professeur.ui;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sinapse.professeur.R;
import com.sinapse.professeur.ui.helper.GradeListForDetailsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassroomChoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassroomChoiceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView gradeListForDetailsRecyclerView;
    private GradeListForDetailsAdapter gradeListForDetailsAdapter;
    private List<String> gradeList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ProgressDialog progressDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClassroomChoiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassroomChoiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassroomChoiceFragment newInstance(String param1, String param2) {
        ClassroomChoiceFragment fragment = new ClassroomChoiceFragment();
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
        String username = getActivity().getIntent().getExtras().getString("username");

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_classroom_choice, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Chargement de la page ...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        db.collection("00000001").document("ac_2019_2020").collection("Professeurs")
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d("ClassroomChoiceFragment", "onComplete");
                if (task.isSuccessful()) {
                    Log.d("ClassroomChoiceFragment", "task.isSuccessful()");
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("ClassroomChoiceFragment", "document.exists()");
                        final ArrayList classrooms = (ArrayList) document.getData().get("classes");
                        if(classrooms != null && classrooms.size() > 0){
                            for (DocumentReference studentRefrence : (ArrayList<DocumentReference>) classrooms) {
                                studentRefrence.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                gradeList.add(document.getId());
                                                Log.d("classroomSize", String.valueOf(gradeList.size()));
                                            }
                                        }
                                    }
                                }).addOnSuccessListener(
                                        new OnSuccessListener<DocumentSnapshot>() {

                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (gradeList.size() == classrooms.size() && gradeList.size() != 0) {
                                                    progressDialog.dismiss();
                                                    Log.d("classroomsSize", String.valueOf(gradeList.size()));
                                                    gradeListForDetailsRecyclerView = rootView.findViewById(R.id.grade_choice_recycler_view);
                                                    gradeListForDetailsAdapter = new GradeListForDetailsAdapter(getContext(), (ArrayList<String>) gradeList);
                                                    gradeListForDetailsRecyclerView.setAdapter(gradeListForDetailsAdapter);
                                                    progressDialog.dismiss();
                                                    gradeListForDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                }
                                            }
                                        }
                                );
                            }
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Aucune classe retrouvee", Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {Log.d("ClassroomChoiceFragment", "!document.exists()");}
                }

                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        return rootView;
    }
}