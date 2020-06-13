package com.sinapse.direction.ui.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sinapse.direction.ui.DrawerAdminFragment;
import com.sinapse.direction.ui.DrawerContentFragment;
import com.sinapse.direction.ui.DrawerMessageFragment;
import com.sinapse.direction.ui.DrawerScheduleFragment;
import com.sinapse.direction.ui.PresenceHomeFragment;
import com.sinapse.direction.ui.SchoolFragment;

public class AdminPagerAdapter extends FragmentStateAdapter {

    public AdminPagerAdapter(Fragment fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PresenceHomeFragment();
            case 1:
                return new SchoolFragment();
            case 2:
                return new SchoolFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
