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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.LoginModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.utils.Constants;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.GlobalVariable;
import com.krishiv.myislam.utils.LocaleHelper;
import com.krishiv.myislam.utils.Shared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity{

    Context context;
    Button btn_login;
    EditText edt_email, edt_pwd;
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
        setContentView(R.layout.login_activity);

        context = this;

        edt_email = findViewById(R.id.edt_email);
        edt_pwd = findViewById(R.id.edt_pwd);
        btn_login = findViewById(R.id.btn_login);
        txt_terms = findViewById(R.id.txt_terms);



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid())
                    callApi(edt_email.getText().toString().trim().toString(),
                            edt_pwd.getText().toString().trim().toString(),
                            "password","ngAuthApp");
            }
        });

        findViewById(R.id.txt_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ActivitySignUp.class));
                finish();
            }
        });

        findViewById(R.id.txt_forgot_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ForgotPass.class));
            }
        });

        setTextSpanable();
        usingFB();
        usingGP();
    }

    private void callApi(String username, String password, String grand_type, String client_id) {

        GlobalTask.showProgressDialog(context);
        Api.getClient().Login(username, password, grand_type, client_id).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                LoginModel resultObj = response.body();
                if (resultObj != null){
                    resultObj.setSubscriptioncomplete(true);
                    new Shared(context).putString(Shared.userProfile, new Gson().toJson(resultObj).toString());

                    startAlarm();

                    Intent intent = new Intent(context, Dashboard.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else {
                    GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                GlobalTask.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                GlobalTask.hideProgressDialog();
            }
        });
    }

    private void startAlarm() {
        Shared shared = new Shared(this);
        shared.putString(shared.upcomingAlarmData,"");
    }

    public boolean isValid() {
        if (edt_email.getText().toString().length()<=0){
            GlobalTask.showToastMessage(this, getResources().getString(R.string.valid_id));
            return false;
        }
        if (edt_pwd.getText().toString().length()<=0){
            GlobalTask.showToastMessage(this, getResources().getString(R.string.valid_pass));
            return false;
        }
        return true;
    }


    CallbackManager callbackManager;
    private void usingFB() {
        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton) findViewById(R.id.btn_login_fb);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    if (email.contentEquals("") || email == null) {
                                        email = "";
                                    }
                                    GlobalVariable.getRegistrationModel().setFirstName(object.getString("first_name"));
                                    GlobalVariable.getRegistrationModel().setLastName(object.getString("last_name"));
                                    GlobalVariable.getRegistrationModel().setEmail(email.contentEquals("")?"":email);
                                    GlobalVariable.getRegistrationModel().setUserName(email.contentEquals("")?object.getString("id"):email);
                                    GlobalVariable.getRegistrationModel().setPassword("");
                                    GlobalVariable.getRegistrationModel().setProvider(Constants.SocialKeyFacebook);
                                    GlobalVariable.getRegistrationModel().setExternalUserId(object.getString("id"));

                                    LoginManager.getInstance().logOut();
                                    callApi();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    GlobalTask.hideProgressDialog();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,last_name,first_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                GlobalTask.hideProgressDialog();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                GlobalTask.hideProgressDialog();
            }
        });

        findViewById(R.id.img_fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
                GlobalTask.showProgressDialog(context);
            }
        });
    }

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    private void usingGP(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.img_gp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GlobalTask.showProgressDialog(context);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 101:
                    try {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        updateUI(account);
                    } catch (ApiException e) {
                        Log.w("", "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;

                default:
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }else{
            GlobalTask.hideProgressDialog();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void callApi() {
        //GlobalTask.showProgressDialog(context);

        Api.getClient().ExternalLogin(GlobalVariable.getRegistrationModel()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                    jsonObject.addProperty("subscriptioncomplete", true);
                    new Shared(context).putString(Shared.userProfile, jsonObject.toString());

                    startAlarm();

                    Intent intent = new Intent(context, Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

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

    private void updateUI(GoogleSignInAccount account) {

        String email = account.getEmail();
        if (email.contentEquals("") || email == null) {
            email = "";
        }
        GlobalVariable.getRegistrationModel().setFirstName(account.getGivenName());
        GlobalVariable.getRegistrationModel().setLastName(account.getFamilyName());
        GlobalVariable.getRegistrationModel().setEmail(email.contentEquals("")?"":email);
        GlobalVariable.getRegistrationModel().setUserName(email.contentEquals("")?account.getId():email);
        GlobalVariable.getRegistrationModel().setPassword("");
        GlobalVariable.getRegistrationModel().setProvider(Constants.SocialKeyGoogle);
        GlobalVariable.getRegistrationModel().setExternalUserId(account.getId());

        mGoogleSignInClient.signOut();
        GlobalTask.showProgressDialog(context);
        callApi();
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

        ((TextView)dialog.findViewById(R.id.txt_title)).setText(getResources().getString(R.string.terms_condition_title));
        ((TextView)dialog.findViewById(R.id.txt_desc)).setText(getResources().getString(R.string.terms_condition_desc));

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
