package com.sinapse.direction.ui.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.sinapse.direction.R;

public class ContentViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_viewer);

        WebView webView =  findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(true);
        //webView.getSettings().setAllowContentAccess(true);
        //webView.getSettings().setDomStorageEnabled(true);
        //webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl("file:///storage/emulated/0/Download/Content/.content"+ getIntent().getExtras().getString("path"));
    }
}
