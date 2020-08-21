package com.sinapse.direction.ui.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sinapse.direction.ui.ClassCreationFragment;
import com.sinapse.direction.ui.ClasseValidationFragment;
import com.sinapse.direction.ui.DrawerAdminFragment;
import com.sinapse.direction.ui.DrawerContentFragment;
import com.sinapse.direction.ui.DrawerMessageFragment;
import com.sinapse.direction.ui.DrawerScheduleFragment;
import com.sinapse.direction.ui.PresenceHomeFragment;
import com.sinapse.direction.ui.ProfileManagementFragment;
import com.sinapse.direction.ui.SchoolFragment;
import com.sinapse.direction.ui.UserManagementFragment;

public class AdminPagerAdapter extends FragmentStateAdapter {

    public AdminPagerAdapter(Fragment fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProfileManagementFragment();
            case 1:
                return new ClassCreationFragment();
            case 2:
                return new ClasseValidationFragment();
            default:
                return new ProfileManagementFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
