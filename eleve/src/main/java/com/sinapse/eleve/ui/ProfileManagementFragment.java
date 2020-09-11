package com.sinapse.eleve.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
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
import com.google.firestore.v1.Document;
import com.sinapse.eleve.R;
import com.sinapse.eleve.ui.helper.TeachersByClassroomAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_LONG;

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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> gradeList = new ArrayList<>();
    Spinner gradeSpinner;

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

        String username = getActivity().getIntent().getExtras().getString("username");
        final Button schoolProfileBtn = rootView.findViewById(R.id.profile_update_btn);
        schoolProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileBtnClicked(v);
            }
        });
        gradeSpinner = (Spinner) rootView.getRootView().findViewById(R.id.spinner_grade_name);

        final TextView nom = rootView.findViewById(R.id.tv_nom);
        final TextView prenom = rootView.findViewById(R.id.tv_prenom);
        //final TextView adresse = rootView.findViewById(R.id.tv_adresse);
        final TextView telephone = rootView.findViewById(R.id.tv_telephone);
        final TextView responsableName = rootView.findViewById(R.id.tv_responsable_name);
        final TextView responsableTel = rootView.findViewById(R.id.tv_responsable_tel);
        db.collection("00000001").document("ac_2019_2020").collection("Classes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                gradeList.add(document.getId());
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                }).addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>(){
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("TAG getting documents.", String.valueOf(gradeList.size()));
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, gradeList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gradeSpinner.setAdapter(adapter);
                    }
                }
        );

        db.collection("00000001").document("ac_2019_2020").collection("Eleves")
                .document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                    }
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData() != null) {
                    nom.setText(nom.getText() +" " + documentSnapshot.getData().get("nom").toString());
                    prenom.setText(prenom.getText() +" " + documentSnapshot.getData().get("prenom").toString());
                    //adresse.setText(document.getData().get("adresse").toString());
                    telephone.setText(telephone.getText() +" " + documentSnapshot.getData().get("telephone").toString());
                    responsableName.setText(responsableName.getText() +" " + documentSnapshot.getData().get("responsable").toString());
                    responsableTel.setText(responsableTel.getText() +" " + documentSnapshot.getData().get("telephone responsable").toString());
                }
            }
        });

        //ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, gradeList);
        //spinnerAdapter.notifyDataSetChanged();
        //spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //gradeSpinner.setAdapter(spinnerAdapter);

        //gradeSpinner.setOnItemSelectedListener(this);
        //Toast.makeText(getContext(), , Toast.LENGTH_LONG).show();
        //Toast.makeText(getContext(), getActivity().getIntent().getExtras().getString("grade"), Toast.LENGTH_LONG).show();
        //Toast.makeText(getContext(), getActivity().getIntent().getExtras().getString("username"), Toast.LENGTH_LONG).show();
        return rootView;
    }

    public void onProfileBtnClicked(View view) {
        Intent intent = new Intent(getContext(), ProfileUpdate.class);
        String username = getActivity().getIntent().getExtras().getString("username");
        intent.putExtra("username", username);
        getActivity().startActivity(intent);
    }
}