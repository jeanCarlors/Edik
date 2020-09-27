package com.sinapse.professeur.classroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sinapse.libmodule.beans.Session;
import com.sinapse.professeur.R;
import com.sinapse.professeur.databinding.FragmentClassroomDetailsBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassroomDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassroomDetailsFragment extends Fragment {

    public ClassroomDetailsFragment() {
        // Required empty public constructor
    }

    FragmentClassroomDetailsBinding binding;
    static final String KEY_PARAM = "CLASS_PATH";
    String path;

    boolean _details = false;
    boolean _list = false;

    public static ClassroomDetailsFragment newInstance(String param_path) {
        ClassroomDetailsFragment fragment = new ClassroomDetailsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_PARAM, param_path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            path = getArguments().getString(KEY_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentClassroomDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseFirestore.getInstance()
                .document(path)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        updateView(documentSnapshot);
                    }
                });

    }

    private void updateView(DocumentSnapshot doc) {
        binding.className.setText(doc.getString("name"));
        _details = true;
        updateProgressBar();

        doc.getReference()
                .collection("cours")
                .whereEqualTo("prof", Session.currentUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(DocumentSnapshot doc: queryDocumentSnapshots.getDocuments()) {
                            if(stringBuilder.length() > 0)
                                stringBuilder.append(" | ");
                            stringBuilder.append(doc.getString("name"));
                        }
                        binding.listCourseName.setText(stringBuilder.toString());
                        _list = true;
                        updateProgressBar();
                    }
                });

        try {
            binding.nbEleves.setText(String.valueOf(((List<Object>)doc.get("eleves")).size()));
        } catch (Exception e) {
            e.printStackTrace();
            binding.nbEleves.setText("N/A");
        }
    }

    private void updateProgressBar(){
        binding.progressCircular.setVisibility(
                (_details && _list)
                        ? View.GONE : View.VISIBLE
        );
    }
}