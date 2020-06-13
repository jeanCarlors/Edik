package com.sinapse.direction.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sinapse.direction.R;
import com.sinapse.direction.ui.helper.PagerAdapter;

public class Home extends FragmentActivity {

    private TextView textView;
    private ViewPager2 viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Contenus"));
        tabLayout.addTab(tabLayout.newTab().setText("Etablissement"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pagerAdapter = new PagerAdapter(this);

        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if(position == 0){
                            tab.setText("Contenus");
                        }else{
                            tab.setText("Etablissement");
                        }
                    }
                }
        ).attach();

    }
}
