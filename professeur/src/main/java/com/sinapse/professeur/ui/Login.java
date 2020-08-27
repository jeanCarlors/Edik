package com.sinapse.professeur.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sinapse.professeur.R;

public class Login extends AppCompatActivity {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginBtnClicked(View view) {
        EditText username = findViewById(R.id.username_edit_text);
        Intent intent = new Intent(this, DrawerHome.class);
        intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    public void OnRecoveryBtnClicked(View view) {
        Intent intent = new Intent(this, LoginRecovery.class);
        startActivity(intent);
    }
}
