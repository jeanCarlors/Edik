package com.sinapse.eleve.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.sinapse.eleve.R;

public class Login extends AppCompatActivity {
    EditText userEditText;
    EditText passwordEditText;
    EditText gradeEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginBtnClicked(View view) {
        gradeEditText = findViewById(R.id.gradeEditText);
        String grade = gradeEditText.getText().toString();
        Intent intent = new Intent(this, DrawerHome.class);
        intent.putExtra("grade", "/" + grade);
        startActivity(intent);
    }

    public void OnRecoveryBtnClicked(View view) {
        Intent intent = new Intent(this, LoginRecovery.class);
        startActivity(intent);
    }
}
