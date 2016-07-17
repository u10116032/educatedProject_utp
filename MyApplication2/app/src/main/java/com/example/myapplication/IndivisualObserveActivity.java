package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class IndivisualObserveActivity extends AppCompatActivity {
    private String webUrl;
    private WebView web;
    private WebViewClient webViewClient;
    private String user;
    private SharedPreferences account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indivisual_observe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        account=getSharedPreferences("account",0);
        user=account.getString("account","");
        webUrl =  "http://163.21.245.192/u10116032/teacher/indivisual_observe.php?student="+user;


        webViewClient = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        web = (WebView)findViewById(R.id.webView);
        web.setWebViewClient(webViewClient);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(webUrl);
    }

}
