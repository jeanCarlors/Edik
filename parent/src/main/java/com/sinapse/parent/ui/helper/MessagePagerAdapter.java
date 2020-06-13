package com.sinapse.parent.ui.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sinapse.parent.ui.DrawerContentFragment;


public class MessagePagerAdapter extends FragmentStateAdapter {
    public MessagePagerAdapter(Fragment fa) {
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
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
