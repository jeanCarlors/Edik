package com.sinapse.professeur.ui.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.sinapse.professeur.R;

public class ContentViewer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_viewer);

        WebView webView =  findViewById(R.id.webView);
        webView.loadUrl("file:///storage/emulated/0/Download/Content/.content"+ getIntent().getExtras().getString("path"));
    }
}
