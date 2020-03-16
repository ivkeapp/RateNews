package com.example.ratenews;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;


public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView mWebView = findViewById(R.id.webview);
        String url = getIntent().getStringExtra("url");

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //ActionBar ab = getSupportActionBar();
        //setSupportActionBar(toolbar);
        mWebView.loadUrl(url);
        //ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
