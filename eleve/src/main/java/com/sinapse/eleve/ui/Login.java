package com.sinapse.eleve.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.sinapse.eleve.R;

public class Login extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    EditText gradeEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginBtnClicked(View view) {
        gradeEditText = findViewById(R.id.gradeEditText);
        usernameEditText = findViewById(R.id.username_edit_text);
        String grade = gradeEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        Intent intent = new Intent(this, DrawerHome.class);
        intent.putExtra("grade", "/" + grade);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void OnRecoveryBtnClicked(View view) {
        Intent intent = new Intent(this, LoginRecovery.class);
        startActivity(intent);
    }
}
