package com.sinapse.direction.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.sinapse.direction.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginBtnClicked(View view) {
        Intent intent = new Intent(this, DrawerHome.class);
        EditText username = (EditText)view.getRootView().findViewById(R.id.user_edit_text);
        //Log.d("username", username.getText().toString());
        intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    public void OnRecoveryBtnClicked(View view) {
        Intent intent = new Intent(this, LoginRecovery.class);
        startActivity(intent);
    }
}
