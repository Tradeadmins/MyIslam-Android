package com.krishiv.myislam.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.GlobalVariable;
import com.krishiv.myislam.utils.LocaleHelper;
import com.krishiv.myislam.utils.Shared;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySignUp extends AppCompatActivity {

    EditText edt_fname, edt_lname, edt_email, edt_pwd, edt_cpwd;
    Button btn_signup;
    Context context;
    TextView txt_terms;

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
        setContentView(R.layout.activity_signup);

        context = this;

        edt_fname = findViewById(R.id.edt_fname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_email = findViewById(R.id.edt_email);
        edt_pwd = findViewById(R.id.edt_pwd);
        edt_cpwd = findViewById(R.id.edt_cpwd);
        btn_signup = findViewById(R.id.btn_signup);

        txt_terms = findViewById(R.id.txt_terms);


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isValid())
                    return;

                GlobalVariable.getRegistrationModel().setFirstName(edt_fname.getText().toString());
                GlobalVariable.getRegistrationModel().setLastName(edt_lname.getText().toString());
                GlobalVariable.getRegistrationModel().setEmail(edt_email.getText().toString());
                GlobalVariable.getRegistrationModel().setUserName(edt_email.getText().toString());
                GlobalVariable.getRegistrationModel().setPassword(edt_pwd.getText().toString());

                GlobalVariable.getRegistrationModel().setProvider("");

                //checkIsEmailAvailable();
//                callRegistrationApi();
                if (isPopupOpen) {
                    callRegistrationApi();
                } else
                    openPopup(1);

                //startActivity(new Intent(context, Subscription.class));
            }
        });

        findViewById(R.id.txt_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySignUp.this, Login.class));
                finish();
            }
        });

        setTextSpanable();
    }

    boolean isPopupOpen = false;

    private void openPopup(final int actionFor) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.popup_privacy_policy);
        dialog.setCancelable(false);

        TextView txt_title, txt_desc;
        txt_title = dialog.findViewById(R.id.txt_title);
        txt_desc = dialog.findViewById(R.id.txt_desc);

        txt_title.setText(actionFor == 1 ? getResources().getString(R.string.privacy_policy_title) : getResources().getString(R.string.terms_condition_title));
        txt_desc.setText(actionFor == 1 ? getResources().getString(R.string.privacy_policy_desc) : getResources().getString(R.string.terms_condition_desc));

        dialog.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (actionFor == 1)
                    openPopup(2);
                else {
                    isPopupOpen = true;
                    callRegistrationApi();
                }
            }
        });

        dialog.findViewById(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void callRegistrationApi() {
        GlobalTask.showProgressDialog(context);

        Log.e("REQ_DATA", new Gson().toJson(GlobalVariable.getRegistrationModel()));

        Api.getClient().Register(GlobalVariable.getRegistrationModel()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                        JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                        new Shared(context).putString(Shared.userProfile, jsonObject.toString());
                        Intent intent = new Intent(context, Subscription.class);
                        startActivity(intent);
                    }
                } else {
                    try {
                        String data = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.has("errorMessage"))
                            GlobalTask.showToastMessage(context, jsonObject.getString("errorMessage"));
/*
                        if (jsonObject.has("statusCode")) {
                            if (jsonObject.getInt("statusCode") == 400) {
                                Intent intent = new Intent(context, Welcome.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                GlobalTask.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                GlobalTask.hideProgressDialog();
            }
        });
    }

    private void checkIsEmailAvailable() {
        GlobalTask.showProgressDialog(this);
        Api.getClient().CheckUnique(GlobalVariable.getRegistrationModel().getEmail()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        if (resultObj.getContent().toString().contains("Not")) {
                            startActivity(new Intent(context, Subscription.class));
                        } else {
                            GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                        }
                    }
                }

                GlobalTask.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                GlobalTask.hideProgressDialog();
            }
        });
    }

    private boolean isValid() {
        if (edt_fname.getText().toString().length() <= 0) {
            GlobalTask.showToastMessage(this, getResources().getString(R.string.valid_fname));
            return false;
        }
        if (edt_lname.getText().toString().length() <= 0) {
            GlobalTask.showToastMessage(this, getResources().getString(R.string.valid_lname));
            return false;
        }
        if (edt_email.getText().toString().length() <= 0) {
            GlobalTask.showToastMessage(this, getResources().getString(R.string.valid_email));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString()).matches()) {
            //return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString()).matches());
            GlobalTask.showToastMessage(this, getResources().getString(R.string.valid_email_exp));
            return false;
        }
        if (edt_pwd.getText().toString().length() <= 5) {
            GlobalTask.showToastMessage(this, getResources().getString(R.string.valid_pass_exp));
            return false;
        }
        if (!edt_cpwd.getText().toString().contentEquals(edt_pwd.getText().toString())) {
            GlobalTask.showToastMessage(this, getResources().getString(R.string.valid_conf_pass));
            return false;
        }
        return true;
    }

    private void setTextSpanable() {
        String strText = txt_terms.getText().toString();
        String clickOn = getResources().getString(R.string.terms);
        SpannableString spannableString = new SpannableString(strText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                openPopup();
            }
        };
        int startIndexOfLink = strText.indexOf(clickOn);
        spannableString.setSpan(clickableSpan, startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorYellow)), startIndexOfLink,
                startIndexOfLink + clickOn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_terms.setHighlightColor(Color.TRANSPARENT);
        txt_terms.setMovementMethod(LinkMovementMethod.getInstance());
        txt_terms.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    private void openPopup() {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.popup_privacy_policy);

        ((TextView) dialog.findViewById(R.id.txt_title)).setText(getResources().getString(R.string.terms_condition_title));
        ((TextView) dialog.findViewById(R.id.txt_desc)).setText(getResources().getString(R.string.terms_condition_desc));

        dialog.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
