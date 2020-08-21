package com.sinapse.eleve.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sinapse.eleve.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesByClassroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesByClassroomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CoursesByClassroomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesByClassroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesByClassroomFragment newInstance(String param1, String param2) {
        CoursesByClassroomFragment fragment = new CoursesByClassroomFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_courses_by_classroom, container, false);
        Button courseLauncher = rootView.findViewById(R.id.course_launcher);
        courseLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CourseLauncher.class);
                String classroom = getActivity().getIntent().getExtras().getString("classroom");
                intent.putExtra("classroom", classroom);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }
}