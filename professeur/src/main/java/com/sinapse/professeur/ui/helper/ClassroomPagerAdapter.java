package com.sinapse.professeur.ui.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sinapse.professeur.ui.AssignmentsByClassroomFragment;
import com.sinapse.professeur.ui.CoursesByClassroomFragment;
import com.sinapse.professeur.ui.ExamsByClassroomFragment;
import com.sinapse.professeur.ui.ActivitiesByClassroomFragment;
import com.sinapse.professeur.ui.StudentsByClassroomFragment;

public class ClassroomPagerAdapter extends FragmentStateAdapter {
    public ClassroomPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CoursesByClassroomFragment();
            case 1:
                return new AssignmentsByClassroomFragment();
            case 2:
                return new ExamsByClassroomFragment();
            case 3:
                return new ActivitiesByClassroomFragment();
            case 4:
                return new StudentsByClassroomFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
