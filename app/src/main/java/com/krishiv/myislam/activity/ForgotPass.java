package com.krishiv.myislam.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.LocaleHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPass extends AppCompatActivity implements View.OnClickListener{

    Context context;
    EditText edt_email;

    protected void attachBaseContext(Context newBase) {
        String currentLanguage = LocaleHelper.getLanguage(newBase);
        if(currentLanguage==null)
        {
            currentLanguage = "en";
            super.attachBaseContext(LocaleHelper.setLocale(newBase,currentLanguage));
        }
        else
        {
            super.attachBaseContext(LocaleHelper.setLocale(newBase,currentLanguage));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);

        context = this;
        edt_email = findViewById(R.id.edt_email);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()) {
                    callApi(edt_email.getText().toString().trim().toString());
                }
            }
        });
    }

    private void callApi(String email) {
        GlobalTask.showProgressDialog(context);

        Api.getClient().ForgotPassword(email).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String)
                    {
                        GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                        startActivity(new Intent(context, Login.class));
                        finish();
                    }
                } else {
                    GlobalTask.showTostErrorResponse(context, response.errorBody());
                }

                GlobalTask.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                GlobalTask.hideProgressDialog();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.arrow_back:
                finish();
                break;
        }
    }

    private boolean isValid() {
        return true;
    }
}
