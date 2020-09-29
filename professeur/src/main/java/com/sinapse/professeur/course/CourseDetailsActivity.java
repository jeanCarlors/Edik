package com.sinapse.professeur.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sinapse.professeur.classroom.ClassroomDetailsFragment;
import com.sinapse.professeur.databinding.ActivityCourseDetailsBinding;

import java.util.Locale;

public class CourseDetailsActivity extends AppCompatActivity {

    ActivityCourseDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//R.layout.activity_course_details
        setSupportActionBar(binding.toolbar);

        final String path = getIntent().getStringExtra("PATH");
        if (path == null) {
            setResult(RESULT_CANCELED);
            finish();
        }


        FirebaseFirestore.getInstance()
                .document(path)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        setTitle(String.format(Locale.FRANCE,
                                "Cour de %s",
                                documentSnapshot.getString("name")));
                    }
                });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showView(position, path);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void showView(int i, String path) {
        Fragment fragment = ClassroomDetailsFragment.newInstance(path);
        switch (i) {
            case 0:
            default:
                fragment = CourseDetailsFragment.newInstance(path);
                break;
            case 1:
                fragment = CourseSessionsListFragment.newInstance(path);
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.frameLayout.getId(), fragment)
                .commit();
    }
}