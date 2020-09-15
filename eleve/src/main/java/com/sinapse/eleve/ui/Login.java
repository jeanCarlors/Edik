package com.sinapse.eleve.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sinapse.eleve.R;
import com.sinapse.eleve.databinding.ActivityLoginBinding;
import com.sinapse.libmodule.beans.Session;
import com.sinapse.libmodule.beans.User;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); //R.layout.activity_main
        setSupportActionBar(binding.toolbar);
    }

    public void onLoginBtnClicked(View view) {


        String username = binding.userEditText.getText().toString();
        if (username.length() == 0) {
            binding.userEditText.setError("Utilisateur non valide.");
            return;
        }

        String password = binding.passwordEditText.getText().toString();
        if (password.length() < 5) {
            binding.passwordEditText.setError("Mot de passe non valide.");
            return;
        }

        username = username.replaceAll(" ", "");

        if (!username.contains("@"))
            username += "@edik.ht";


        final ProgressDialog loading = ProgressDialog.show(
                this,
                "Authentification...",
                "Patientez S.V.P ...",
                true,
                false);

        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(username, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loading.dismiss();
                        binding.tvError.setText(e.getMessage());
                        binding.tvError.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        loading.dismiss();
                        binding.tvError.setText("Auth annulée!");
                        binding.tvError.setVisibility(View.VISIBLE);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(final AuthResult authResult) {

                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(authResult.getUser().getUid())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        if (!documentSnapshot.exists()) {
                                            FirebaseAuth.getInstance().signOut();
                                            loading.dismiss();
                                            binding.tvError.setText("Une erreur s'est produite.");
                                            binding.tvError.setVisibility(View.VISIBLE);
                                            return;
                                        }

                                        Session.currentUser = new User();
                                        Session.currentUser.setName(documentSnapshot.getString("name"));
                                        Session.currentUser.setPhoto(documentSnapshot.getString("photo"));
                                        Session.currentUser.setStatus(documentSnapshot.getString("status"));
                                        Session.currentUser.setType(documentSnapshot.getString("type"));
                                        Session.currentUser.setSchool(documentSnapshot.getString("school"));
                                        Session.currentUser.setUid(authResult.getUser().getUid());

                                        try {
                                            Session.currentUser.setVerified(documentSnapshot.getBoolean("verified"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        loading.dismiss();
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        FirebaseAuth.getInstance().signOut();
                                        loading.dismiss();
                                        binding.tvError.setText(e.getMessage());
                                        binding.tvError.setVisibility(View.VISIBLE);
                                        e.printStackTrace();
                                    }
                                });

                    }
                });
    }


    public void OnRecoveryBtnClicked(View view) {
        Intent intent = new Intent(this, LoginRecovery.class);
        startActivity(intent);
    }
}
