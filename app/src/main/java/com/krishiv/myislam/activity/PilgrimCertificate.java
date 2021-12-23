package com.krishiv.myislam.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.krishiv.myislam.R;
import com.krishiv.myislam.menu.BaseMenuActivity;

import org.w3c.dom.Text;

public class PilgrimCertificate extends BaseMenuActivity implements View.OnClickListener {

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_pilgrim_certificate);
        txt = findViewById(R.id.txt);
        setTextSpanable();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;
        }
    }

    private void setTextSpanable() {
        String strText = txt.getText().toString();
        String clickOn = "here";
        SpannableString spannableString = new SpannableString(strText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };
        int startIndexOfLink = strText.indexOf(clickOn);
        spannableString.setSpan(clickableSpan, startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt.setHighlightColor(Color.TRANSPARENT);
        txt.setMovementMethod(LinkMovementMethod.getInstance());
        txt.setText(spannableString, TextView.BufferType.SPANNABLE);
    }
}
