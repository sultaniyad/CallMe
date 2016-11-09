package com.iyad.sultan.callme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class LicenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_licenses);
        WebView webView = (WebView) findViewById(R.id.webView_licence);

        webView.loadUrl("file:///android_asset/licence.html");

    }
}
