package com.sinapse.eleve.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sinapse.eleve.R;
import com.sinapse.eleve.ui.Home;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoginBtnClicked(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
