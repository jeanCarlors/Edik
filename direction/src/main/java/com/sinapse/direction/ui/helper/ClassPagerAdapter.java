package com.sinapse.direction.ui.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sinapse.direction.ui.DrawerContentFragment;
import com.sinapse.direction.ui.SchoolFragment;

public class ClassPagerAdapter extends FragmentStateAdapter {
    public ClassPagerAdapter(Fragment fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DrawerContentFragment();
            case 1:
                return new SchoolFragment();
            case 2:
                return new DrawerContentFragment();
            case 3:
                return new SchoolFragment();
            case 4:
                return new DrawerContentFragment();
            case 5:
                return new SchoolFragment();

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

}
