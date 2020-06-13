package com.sinapse.direction.ui.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sinapse.direction.ui.DrawerContentFragment;
import com.sinapse.direction.ui.PresenceListByClassroomFragment;
import com.sinapse.direction.ui.SchoolFragment;

public class PresencePagerAdapter extends FragmentStateAdapter {
    public PresencePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PresenceListByClassroomFragment();
            case 1:
                return new PresenceListByClassroomFragment();
            case 2:
                return new PresenceListByClassroomFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
