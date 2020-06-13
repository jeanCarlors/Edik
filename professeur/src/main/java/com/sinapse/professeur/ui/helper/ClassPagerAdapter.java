package com.sinapse.professeur.ui.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sinapse.professeur.ui.DrawerContentFragment;


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
                return new DrawerContentFragment();
            case 2:
                return new DrawerContentFragment();
            case 3:
                return new DrawerContentFragment();
            case 4:
                return new DrawerContentFragment();
            case 5:
                return new DrawerContentFragment();

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

}
