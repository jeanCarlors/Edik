package com.sinapse.direction.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sinapse.direction.R;

import java.util.HashMap;
import java.util.Map;

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
        Log.d("username", username);
        Toast.makeText(getContext(), username, Toast.LENGTH_LONG);

        setProfile(username, rootView);
        Button schoolProfileBtn = rootView.findViewById(R.id.school_profile_btn);
        schoolProfileBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onSchoolProfileBtnClicked(v);
            }
        });
        return rootView;
    }

    private void setProfile(String username, View view) {
        final TextView schoolName = view.findViewById(R.id.tv_school_name);
        final TextView schoolAddress = view.findViewById(R.id.tv_school_address);
        final TextView schoolPhone = view.findViewById(R.id.tv_school_phone);
        db.collection(username).document("Profil").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        schoolName.setText(schoolName.getText().toString() + " " + document.getData().get("nom"));
                        schoolAddress.setText(schoolAddress.getText().toString() + " " + document.getData().get("adresse"));
                        schoolPhone.setText(schoolPhone.getText().toString() + " " + document.getData().get("telephone"));
                    } else {
                        Log.d("TAG", "No such document");
                    }
                }
            }
        });
    }


    public void onSchoolProfileBtnClicked(View view){
        String username = getActivity().getIntent().getExtras().getString("username");
        EditText edt_name = (EditText) view.getRootView().findViewById(R.id.edit_text_school_name);
        EditText edt_address = (EditText) view.getRootView().findViewById(R.id.edit_text_school_address);
        EditText edt_telephone = (EditText) view.getRootView().findViewById(R.id.edit_text_school_telephone);

        Map<String, Object> profil = new HashMap<>();
        profil.put("nom", edt_name.getText().toString());
        profil.put("adresse", edt_address.getText().toString());
        profil.put("telephone", edt_telephone.getText().toString());

        Map<String, Object> descriptif = new HashMap<>();
        descriptif.put("descriptif", "Ce champs contient toutes les informations pour l'année académique 2019-2020.");

        db.collection(username).document("Profil").set(profil);
        db.collection(username).document("ac_2019_2020").set(descriptif);

        edt_name.setText("");
        edt_address.setText("");
        edt_telephone.setText("");
        edt_name.setHint("Rentrez le nom de l'école");
        edt_address.setHint("Rentrez l'adresse de l'école");
        edt_telephone.setHint("Rentrez le téléphone de l'école");
        Toast.makeText(getContext(), "Le profil est enregistré", Toast.LENGTH_LONG).show();
    }
}
