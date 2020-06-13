package com.sinapse.direction.ui.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sinapse.direction.ui.DrawerContentFragment;
import com.sinapse.direction.ui.TabTeacherOtherInfoByClassFragment;
import com.sinapse.direction.ui.TabTeachersByClassFragment;


public class TeacherPagerAdapter extends FragmentStateAdapter {
    public TeacherPagerAdapter(Fragment fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TabTeachersByClassFragment();
            case 1:
                return new TabTeacherOtherInfoByClassFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
