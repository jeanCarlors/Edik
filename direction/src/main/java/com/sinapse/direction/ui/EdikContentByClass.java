package com.sinapse.direction.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sinapse.direction.R;
import com.sinapse.direction.databinding.ActivityEdikContentBinding;
import com.sinapse.direction.databinding.ActivityEdikContentByClassBinding;

public class EdikContentByClass extends AppCompatActivity {

    ActivityEdikContentByClassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEdikContentByClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//R.layout.activity_edik_content_by_class
        setSupportActionBar(binding.toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onNSIClicked(View view) {
        Intent intent = new Intent(this, ContentList.class);
        intent.putExtra("grade", "/NS I");
        intent.putExtra("name", "Nouveau Secondaire I");
        startActivity(intent);
    }

    public void onNSIIClicked(View view) {
        Intent intent = new Intent(this, ContentList.class);
        intent.putExtra("grade", "/NS II");
        intent.putExtra("name", "Nouveau Secondaire II");
        startActivity(intent);
    }

    public void onNSIIIClicked(View view) {
        Intent intent = new Intent(this, ContentList.class);
        intent.putExtra("grade", "/NS III");
        intent.putExtra("name", "Nouveau Secondaire III");
        startActivity(intent);
    }

    public void onNSIVClicked(View view) {
        Intent intent = new Intent(this, ContentList.class);
        intent.putExtra("grade", "/NS IV");
        intent.putExtra("name", "Nouveau Secondaire IV");
        startActivity(intent);
    }
}
