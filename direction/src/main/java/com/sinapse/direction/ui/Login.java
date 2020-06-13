package com.sinapse.direction.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sinapse.direction.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginBtnClicked(View view) {
        Intent intent = new Intent(this, DrawerHome.class);
        startActivity(intent);
    }

    public void OnRecoveryBtnClicked(View view) {
        Intent intent = new Intent(this, LoginRecovery.class);
        startActivity(intent);
    }
}
