package com.sinapse.direction.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sinapse.direction.R;
import com.sinapse.direction.ui.helper.TeacherPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DrawerTeacherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawerTeacherFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout tabLayout;
    private TeacherPagerAdapter teacherPagerAdapter;
    private ViewPager2 viewPager;


    public DrawerTeacherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrawerTeacherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrawerTeacherFragment newInstance(String param1, String param2) {
        DrawerTeacherFragment fragment = new DrawerTeacherFragment();
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
        View view = inflater.inflate(R.layout.fragment_drawer_teacher, container, false);
        tabLayout = view.findViewById(R.id.drawer_teacher_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Présence"));
        tabLayout.addTab(tabLayout.newTab().setText("Suspension"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        teacherPagerAdapter = new TeacherPagerAdapter(this);
        viewPager = view.findViewById(R.id.drawer_teacher_pager);
        viewPager.setAdapter(teacherPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch(position) {
                            case 0:
                                tab.setText("Liste");
                                break;
                            case 1:
                                tab.setText("Autres infos");
                                break;
                            default:
                                tab.setText("Liste");
                        }
                    }
                }
        ).attach();
        return view;
    }
}
