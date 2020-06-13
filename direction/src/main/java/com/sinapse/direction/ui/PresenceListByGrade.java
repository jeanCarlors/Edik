package com.sinapse.direction.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sinapse.direction.R;
import com.sinapse.direction.ui.helper.ClassPagerAdapter;
import com.sinapse.direction.ui.helper.PresencePagerAdapter;

public class PresenceListByGrade extends AppCompatActivity {

    private TabLayout tabLayout;
    private PresencePagerAdapter presencePagerAdapter;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence_list_by_grade);

        tabLayout = findViewById(R.id.presence_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Présence"));
        tabLayout.addTab(tabLayout.newTab().setText("Suspension"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        presencePagerAdapter = new PresencePagerAdapter(this);
        viewPager = findViewById(R.id.presence_pager);
        viewPager.setAdapter(presencePagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch(position) {
                            case 0:
                                tab.setText("NS I A");
                                break;
                            case 1:
                                tab.setText("NS I B");
                                break;
                            case 2:
                                tab.setText("NS I C");
                                break;
                            default:
                                tab.setText("NS I A");
                        }
                    }
                }
        ).attach();
    }
}
