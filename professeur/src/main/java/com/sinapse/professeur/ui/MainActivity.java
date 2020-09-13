package com.sinapse.professeur.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sinapse.libmodule.beans.Session;
import com.sinapse.libmodule.beans.User;
import com.sinapse.professeur.R;
import com.sinapse.professeur.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static int LOGIN_ACTIVITY = 123;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); //R.layout.activity_main
        setSupportActionBar(binding.toolbar);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            binding.fab.setText("Entrer");
            binding.fab.setVisibility(View.GONE);

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            Session.currentUser = new User();
                            Session.currentUser.setName(documentSnapshot.getString("name"));
                            Session.currentUser.setPhoto(documentSnapshot.getString("photo"));
                            Session.currentUser.setStatus(documentSnapshot.getString("status"));
                            Session.currentUser.setType(documentSnapshot.getString("type"));
                            Session.currentUser.setSchool(documentSnapshot.getString("school"));
                            Session.currentUser.setUid(user.getUid());

                            try {
                                Session.currentUser.setVerified(documentSnapshot.getBoolean("verified"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            binding.fab.setVisibility(View.VISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseAuth.getInstance().signOut();
                            binding.fab.setText("Se connecter");
                            binding.fab.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    });
        }
    }

    public void onLoginBtnClicked(View view) {
        if(Session.currentUser == null) {
            Intent intent = new Intent(this, Login.class);
            startActivityForResult(intent, LOGIN_ACTIVITY);
        } else {
            goToDrawer();
        }
    }

    private void goToDrawer() {
        Log.d("LOGIN", Session.currentUser.getUid());
        startActivity(new Intent(this, DrawerHome.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_ACTIVITY && resultCode == RESULT_OK) {
            goToDrawer();
        }
    }
}
