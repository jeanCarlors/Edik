package com.sinapse.professeur.ui;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sinapse.professeur.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileManagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileManagementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int subjectNumber;
    ArrayList<String> grades = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileManagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileManagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileManagementFragment newInstance(String param1, String param2) {
        ProfileManagementFragment fragment = new ProfileManagementFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_profile_management, container, false);
        //String username = getActivity().getIntent().getExtras().getString("username");
        //Log.d("username", username);
        //Toast.makeText(getContext(), username, Toast.LENGTH_LONG);
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.sipnner_classroom);

        db.collection("00000001").document("ac_2019_2020").collection("Classes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                grades.add(document.getId());
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                }).addOnSuccessListener(
                        new OnSuccessListener<QuerySnapshot>(){
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Log.d("TAG getting documents.", String.valueOf(grades.size()));
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, grades);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
                                Button schoolProfileBtn = rootView.findViewById(R.id.profile_btn);
                                schoolProfileBtn.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        onProfileBtnClicked(v);
                                    }
                                });
                            }
                        }
                        );


    //s.setOnClickListener((View.OnClickListener) );

        return rootView;
    }



    public void onProfileBtnClicked(View view){
        final String username = getActivity().getIntent().getExtras().getString("username");
        EditText edt_name = (EditText) view.getRootView().findViewById(R.id.edit_text_teacher_name);
        EditText edt_address = (EditText) view.getRootView().findViewById(R.id.edit_text_teacher_address);
        EditText edt_telephone = (EditText) view.getRootView().findViewById(R.id.edit_text_teacher_telephone);

        final Spinner spinner_classroom = (Spinner) view.getRootView().findViewById(R.id.sipnner_classroom);
        EditText edt_schedule = (EditText) view.getRootView().findViewById(R.id.edit_text_teacher_schedule);
        EditText edt_subject = (EditText) view.getRootView().findViewById(R.id.edit_text_teacher_subject);

        final Map<String, Object> profil = new HashMap<>();
        Map<String, Object> matieres = new HashMap<>();
        ArrayList<String> matiere = new ArrayList<String>();
            if(edt_name.getText().toString().length() > 0)
            profil.put("nom", edt_name.getText().toString());

            if(edt_address.getText().toString().length() > 0)
            profil.put("adresse", edt_address.getText().toString());

            if(edt_telephone.getText().toString().length() > 0)
            profil.put("telephone", edt_telephone.getText().toString());
            ArrayList classes = new ArrayList<String>();
            classes.add("");
            profil.put("classes", classes);

        if (edt_name.getText().length() > 0 || edt_address.getText().length() > 0 || edt_telephone.getText().length() > 0) {
            db.collection("00000001").document("ac_2019_2020")
                    .collection("Professeurs")
                    .document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            db.collection("00000001").document("ac_2019_2020")
                                    .collection("Professeurs")
                                    .document(username).update(profil);
                        }else{
                            db.collection("00000001").document("ac_2019_2020")
                                    .collection("Professeurs")
                                    .document(username).set(profil);
                        }
                    }
                }
            });
            edt_name.setText("");
            edt_address.setText("");
            edt_telephone.setText("");
            edt_name.setHint("Rentrez le nom de l'école");
            edt_address.setHint("Rentrez l'adresse de l'école");
            edt_telephone.setHint("Rentrez le téléphone de l'école");
        }

            if(spinner_classroom.getSelectedItem().toString().length() > 0
                  && edt_subject.getText().length() > 0 && edt_schedule.getText().length() > 0){
                        matiere.add(spinner_classroom.getSelectedItem().toString());
                        matiere.add(edt_subject.getText().toString());
                        matiere.add(edt_schedule.getText().toString());
                        matieres.put(edt_subject.getText().toString(), matiere);
                                    db.collection("00000001").document("ac_2019_2020")
                                            .collection("Professeurs")
                                            .document(username).collection("Matieres")
                                            .document(edt_subject.getText().toString())
                                            .set(matieres);

                        DocumentReference professeurDocumentreference = db.collection("00000001")
                                .document("ac_2019_2020").collection("Professeurs")
                                .document(username);
                        db.collection("00000001").document("ac_2019_2020").collection("Classes")
                                .document(spinner_classroom.getSelectedItem().toString()).update("professeurs", FieldValue
                                .arrayUnion(professeurDocumentreference));
                        DocumentReference classeDocumentreference = db.collection("00000001")
                                .document("ac_2019_2020").collection("Classes")
                                .document(spinner_classroom.getSelectedItem().toString());
                        db.collection("00000001").document("ac_2019_2020").collection("Professeurs")
                                .document(username).update("classes", FieldValue.arrayUnion(classeDocumentreference));
                        db.collection("00000001").document("ac_2019_2020").collection("Professeurs")
                                .document(username).update("classes", FieldValue.arrayRemove(""));
                edt_schedule.setText("");
                edt_subject.setText("");
                edt_schedule.setHint("Ex: Mardi 11h-13h");
                edt_subject.setHint("Ex: Algèbre");
            }
    }
}