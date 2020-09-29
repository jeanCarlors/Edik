package com.sinapse.professeur.classroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sinapse.professeur.R;
import com.sinapse.professeur.databinding.ActivityClassroomDetailsBinding;

public class ClassroomDetailsActivity extends AppCompatActivity {

    ActivityClassroomDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClassroomDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); //R.layout.activity_classroom_details
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
                        setTitle(documentSnapshot.getString("name"));
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
                fragment = ClassroomDetailsFragment.newInstance(path);
                break;
            case 1:
                fragment = CourseListFragment.newInstance(path);
                break;
            case 2:
                fragment = StudentsListFragment.newInstance(path);
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.frameLayout.getId(), fragment)
                .commit();
    }
}