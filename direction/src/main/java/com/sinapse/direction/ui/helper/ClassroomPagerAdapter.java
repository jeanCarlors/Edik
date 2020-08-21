package com.sinapse.direction.ui.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sinapse.direction.ui.AssignmentsByClassroomFragment;
import com.sinapse.direction.ui.CoursesByClassroomFragment;
import com.sinapse.direction.ui.DrawerContentFragment;
import com.sinapse.direction.ui.ExamsByClassroomFragment;
import com.sinapse.direction.ui.RatingsByClassroomFragment;
import com.sinapse.direction.ui.SchoolFragment;
import com.sinapse.direction.ui.StudentsByClassroomFragment;
import com.sinapse.direction.ui.TeachersByClassroomFragment;

public class ClassroomPagerAdapter extends FragmentStateAdapter {
    public ClassroomPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new StudentsByClassroomFragment();
            case 1:
                return new TeachersByClassroomFragment();
            case 2:
                return new CoursesByClassroomFragment();
            case 3:
                return new AssignmentsByClassroomFragment();
            case 4:
                return new ExamsByClassroomFragment();
            case 5:
                return new RatingsByClassroomFragment();

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
