package com.sinapse.eleve.ui.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sinapse.eleve.ui.AssignmentsByClassroomFragment;
import com.sinapse.eleve.ui.CoursesByClassroomFragment;
import com.sinapse.eleve.ui.DrawerContentFragment;
import com.sinapse.eleve.ui.ExamsByClassroomFragment;
import com.sinapse.eleve.ui.RatingsByClassroomFragment;
import com.sinapse.eleve.ui.TeachersByClassroomFragment;


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
                return new RatingsByClassroomFragment();
            case 4:
                return new TeachersByClassroomFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

}
