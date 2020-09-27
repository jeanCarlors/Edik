package com.sinapse.professeur.classroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.sinapse.libmodule.beans.Course;
import com.sinapse.professeur.R;
import com.sinapse.professeur.databinding.FragmentStudentsListBinding;
import com.sinapse.professeur.viewholders.ClassViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentsListFragment extends Fragment {

    FragmentStudentsListBinding binding;
    static final String KEY_PARAM = "CLASS_PATH";
    String path;
    FirestoreRecyclerAdapter<Course, ClassViewHolder> adapter;

    public StudentsListFragment() {
        // Required empty public constructor
    }

    public static StudentsListFragment newInstance(String param_path) {
        StudentsListFragment fragment = new StudentsListFragment();
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
        binding = FragmentStudentsListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }
}