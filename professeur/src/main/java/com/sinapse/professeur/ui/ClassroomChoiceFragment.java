package com.sinapse.professeur.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.sinapse.libmodule.beans.Classroom;
import com.sinapse.libmodule.beans.Session;
import com.sinapse.professeur.R;
import com.sinapse.professeur.classroom.ClassroomDetailsActivity;
import com.sinapse.professeur.ui.helper.GradeListForDetailsAdapter;
import com.sinapse.professeur.viewholders.ClassViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<Classroom, ClassViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        String username = getActivity().getIntent().getExtras().getString("username");

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_classroom_choice, container, false);

        Query query = db.collection("Schools")
                .document(Session.currentUser.getSchool())
                .collection("annees_academiques")
                .document(Session.currentUser.getAnneeAcademique())
                .collection("classes")
                .whereArrayContains("professeurs", Session.currentUser.getUid());

        FirestoreRecyclerOptions<Classroom> classrooms = new FirestoreRecyclerOptions.Builder<Classroom>()
                .setQuery(query, new SnapshotParser<Classroom>() {
                    @NonNull
                    @Override
                    public Classroom parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Classroom classroom = new Classroom();
                        classroom.setName(snapshot.getString("name"));
                        classroom.setDocPath(snapshot.getReference().getPath());
                        try {
                            classroom.setNbEleves(((List<Object>)snapshot.get("eleves")).size());
                        } catch (Exception e) {
                            classroom.setNbEleves(-1);
                            e.printStackTrace();
                        }
                        return classroom;
                    }
                })
                .build();

        adapter = new FirestoreRecyclerAdapter<Classroom, ClassViewHolder>(classrooms) {
            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                e.printStackTrace();
            }

            @Override
            protected void onBindViewHolder(@NonNull ClassViewHolder holder, int position, @NonNull final Classroom model) {
                holder.title.setText(model.getName());
                holder.subTitle.setText(String.format(Locale.FRANCE, "Nbre d'éleves: %d", model.getNbEleves()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Class pa0th", model.getDocPath());
                        Intent intent = new Intent(getContext(), ClassroomDetailsActivity.class);
                        intent.putExtra("PATH", model.getDocPath());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_2, parent, false);
                return new ClassViewHolder(view);
            }
        };

        recyclerView = rootView.findViewById(R.id.grade_choice_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        /*progressDialog = new ProgressDialog(getContext());
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
                                if (classrooms != null && classrooms.size() > 0) {
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
                                        })
                                        .addOnSuccessListener(
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
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Aucune classe retrouvee", Toast.LENGTH_LONG)
                                            .show();
                                }
                            } else {
                                Log.d("ClassroomChoiceFragment", "!document.exists()");
                            }
                        }

                        progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });*/

        return rootView;

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