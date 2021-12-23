package com.krishiv.myislam.activity;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.krishiv.myislam.R;
import com.krishiv.myislam.menu.BaseMenuActivity;

import java.io.IOException;
import java.io.InputStream;

public class ProphetMuhmmad extends BaseMenuActivity implements View.OnClickListener{
    WebView webView;

    public String fileName = "prophet.html";
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.profet_mohhamed);
        webView = (WebView) findViewById(R.id.simpleWebView);
        ((TextView)findViewById(R.id.txt_title)).setText(getResources().getString(R.string.prophet_mohammed_final_msg));

        // displaying content in WebView from html file that stored in assets folder
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + fileName);
        webView.setBackgroundResource(R.drawable.bg_edt_yellow);
        webView.setPadding(1,1,30,1);
        webView.setBackgroundColor(getResources().getColor(R.color.colorLightYellow));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView web, String url) {
                webView.loadUrl("javascript:document.body.style.margin=\"6%\"; void 0");
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.arrow_back:
                finish();
                break;
        }
    }
}
