package com.sinapse.direction.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sinapse.direction.R;
import com.sinapse.direction.ui.helper.GradeCreatedAdapter;
import com.sinapse.direction.ui.helper.GradeListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassCreationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassCreationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> gradeList = new ArrayList<>();
    private RecyclerView gradeCreatedRecyclerView;
    private GradeCreatedAdapter gradeCreatedAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClassCreationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassCreationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassCreationFragment newInstance(String param1, String param2) {
        ClassCreationFragment fragment = new ClassCreationFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_class_creation, container, false);
        String username = getActivity().getIntent().getExtras().getString("username");

        Button gradeAddingBtn = rootView.findViewById(R.id.grade_adding_btn);
        gradeAddingBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onGradeAddingBtnClicked(v);
            }
        });




        db.collection("00000001").document("ac_2019_2020").collection("Classes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                gradeList.add(document.getId());
                                Log.d("Document", document.getId() );
                            }

                            gradeCreatedRecyclerView = rootView.findViewById(R.id.grade_created_recycler_view);
                            //progressDialog.dismiss();
                            //Collections.reverse(gradeList);
                            gradeCreatedAdapter = new GradeCreatedAdapter(getContext(), (ArrayList<String>) gradeList);
                            gradeCreatedRecyclerView.setAdapter(gradeCreatedAdapter);
                            gradeCreatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        } else {
                            Log.d("Document1", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return rootView;
    }

    public void onGradeAddingBtnClicked(View view){
        String username = getActivity().getIntent().getExtras().getString("username");
        EditText edtSchoolGrade = (EditText) view.getRootView().findViewById(R.id.edit_text_school_grade);
        EditText edtSchoolGradeName = (EditText) view.getRootView().findViewById(R.id.edit_text_school_grade_name);

        Map<String, Object> descriptif = new HashMap<>();
        descriptif.put("descriptif", "Il s'agit d'une salle de classe");
        String gradePath = edtSchoolGrade.getText().toString() + " " + edtSchoolGradeName.getText().toString();

        db.collection(username).document("ac_2019_2020").collection("Classes")
                .document(gradePath).set(descriptif);

        edtSchoolGrade.setText("");
        edtSchoolGradeName.setText("");
        edtSchoolGrade.setHint("Exemple: NS I");
        edtSchoolGradeName.setHint("Exemple: A, B ou 1, 2");
        Toast.makeText(getContext(), "La classe " + gradePath + " est créée", Toast.LENGTH_LONG).show();
    }
}