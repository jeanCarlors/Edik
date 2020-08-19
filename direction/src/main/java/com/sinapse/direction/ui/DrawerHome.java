package com.sinapse.direction.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sinapse.direction.R;
import com.sinapse.direction.databinding.ActivityDrawerHomeBinding;

public class DrawerHome extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    //private DrawerToggle drawerToggle;

    ActivityDrawerHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDrawerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); //R.layout.activity_drawer_home

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawerToggle().setDrawerIndicatorEnabled(true);
        setupDrawerToggle().syncState();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_drawer_content, new DrawerContentFragment());
        fragmentTransaction.commit();

        nvDrawer = (NavigationView) findViewById(R.id.nv_view);
        setupDrawerContent(nvDrawer);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            ImageView img = binding.nvView.getHeaderView(0).findViewById(R.id.drawer_header_img);
            TextView name = binding.nvView.getHeaderView(0).findViewById(R.id.drawer_header_name);
            TextView school = binding.nvView.getHeaderView(0).findViewById(R.id.drawer_header_school);

            Glide.with(this).load(user.getPhotoUrl()).circleCrop().into(img);
            name.setText(user.getDisplayName());
            school.setText(user.getEmail());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.drawer_Content:
            default:
                fragmentClass = DrawerContentFragment.class;
                break;
            case R.id.drawer_admin:
                fragmentClass = DrawerAdminFragment.class;
                break;
            case R.id.drawer_classroom:
                fragmentClass = DrawerClassFragment.class;
                break;
            case R.id.drawer_teacher:
                fragmentClass = DrawerTeacherFragment.class;
                break;
            case R.id.drawer_schedule:
                fragmentClass = DrawerScheduleFragment.class;
                break;
            case R.id.drawer_message:
                fragmentClass = DrawerMessageFragment.class;
                break;
            case R.id.drawer_logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                            }
                        });
                return;

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.home_drawer_content, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }
}
