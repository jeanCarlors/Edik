package com.sinapse.eleve.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sinapse.eleve.R;
import com.sinapse.eleve.ui.helper.ClassroomPagerAdapter;

public class Classroom extends AppCompatActivity {
    private TabLayout tabLayout;
    private ClassroomPagerAdapter classroomPagerAdapter;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        tabLayout = findViewById(R.id.drawer_class_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Présence"));
        tabLayout.addTab(tabLayout.newTab().setText("Suspension"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        classroomPagerAdapter = new ClassroomPagerAdapter(this);
        viewPager = findViewById(R.id.drawer_class_pager);
        viewPager.setAdapter(classroomPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch(position) {
                            case 0:
                                tab.setText("Cours");
                                break;
                            case 1:
                                tab.setText("Devoirs");
                                break;
                            case 2:
                                tab.setText("Controles");
                                break;
                            case 3:
                                tab.setText("Notes");
                                break;
                            case 4:
                                tab.setText("Professeurs");
                                break;
                            default:
                                tab.setText("Cours");
                        }
                    }
                }
        ).attach();

    }

}
