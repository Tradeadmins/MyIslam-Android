package com.krishiv.myislam.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.android.vending.billing.IInAppBillingService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.LoginModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.dto.SubscriptionModel;
import com.krishiv.myislam.utilpay.IabBroadcastReceiver;
import com.krishiv.myislam.utilpay.IabHelper;
import com.krishiv.myislam.utilpay.IabResult;
import com.krishiv.myislam.utilpay.Inventory;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.utils.Constants;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.GlobalVariable;
import com.krishiv.myislam.utils.LocaleHelper;
import com.krishiv.myislam.utils.Shared;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.krishiv.myislam.utilpay.IabHelper.BILLING_RESPONSE_RESULT_OK;
import static com.krishiv.myislam.utilpay.IabHelper.RESPONSE_BUY_INTENT;
import static com.krishiv.myislam.utilpay.IabHelper.RESPONSE_CODE;

public class Subscription extends AppCompatActivity implements PurchasesUpdatedListener {

    Context context;
    Button btn_plan;
    TextView txt_trial;
    LinearLayout lnr_btn_plan1, lnr_btn_plan2;
    static boolean isPopupOpen = false;
    ProgressDialog progressdialog;
    String mResultSku;

    /*in-app*/
    static final String TAG = "In-App Purchase";
    boolean mSubscribed = false;
    static final int RC_REQUEST = 10001;
    private final int GOOGLE_BILLING_CODE = 1001;
    BillingClient mBillingClient;
    List<SkuDetails> skuListDetail;

    protected void attachBaseContext(Context newBase) {
        String currentLanguage = LocaleHelper.getLanguage(newBase);
        if (currentLanguage == null) {
            currentLanguage = "en";
            super.attachBaseContext(LocaleHelper.setLocale(newBase, currentLanguage));
        } else {
            super.attachBaseContext(LocaleHelper.setLocale(newBase, currentLanguage));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_subscription);

        context = this;
        mBillingClient = BillingClient.newBuilder(context).setListener(this).build();
        startServiceConnection();

        btn_plan = findViewById(R.id.btn_plan);
        txt_trial = findViewById(R.id.txt_trial);
        lnr_btn_plan1 = findViewById(R.id.lnr_btn_plan1);
        lnr_btn_plan2 = findViewById(R.id.lnr_btn_plan2);

        findViewById(R.id.lnr_btn_trial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.getRegistrationModel().setSubscriptionType("Trail");
                mResultSku = Constants.SkuTrial;
                choosePlan(0);
            }
        });

        lnr_btn_plan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.getRegistrationModel().setSubscriptionType("Monthly");
                mResultSku = Constants.SkuMonth;
                choosePlan(1);
            }
        });

        lnr_btn_plan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.getRegistrationModel().setSubscriptionType("Yearly");
                mResultSku = Constants.SkuYear;
                choosePlan(2);
            }
        });

        //callApi();

        /*In-App */
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");

    }

    void choosePlan(int plan) {

        if (plan == 0) {
            txt_trial.setBackground(getResources().getDrawable(R.drawable.bg_btn_yellow));
            txt_trial.setTextColor(getResources().getColor(R.color.colorBlack));
            ChangeColorOf(lnr_btn_plan1, getResources().getColor(R.color.colorYellow));
            ChangeColorOf(lnr_btn_plan2, getResources().getColor(R.color.colorYellow));
            lnr_btn_plan1.setBackground(getResources().getDrawable(R.drawable.bg_btn_border_yellow));
            lnr_btn_plan2.setBackground(getResources().getDrawable(R.drawable.bg_btn_border_yellow));
        } else if (plan == 1) {
            txt_trial.setBackgroundColor(Color.TRANSPARENT);
            txt_trial.setTextColor(getResources().getColor(R.color.colorYellow));
            ChangeColorOf(lnr_btn_plan1, getResources().getColor(R.color.colorBlack));
            ChangeColorOf(lnr_btn_plan2, getResources().getColor(R.color.colorYellow));
            lnr_btn_plan1.setBackground(getResources().getDrawable(R.drawable.bg_btn_yellow));
            lnr_btn_plan2.setBackground(getResources().getDrawable(R.drawable.bg_btn_border_yellow));
        } else if (plan == 2) {
            txt_trial.setBackgroundColor(Color.TRANSPARENT);
            txt_trial.setTextColor(getResources().getColor(R.color.colorYellow));
            ChangeColorOf(lnr_btn_plan1, getResources().getColor(R.color.colorYellow));
            ChangeColorOf(lnr_btn_plan2, getResources().getColor(R.color.colorBlack));
            lnr_btn_plan1.setBackground(getResources().getDrawable(R.drawable.bg_btn_border_yellow));
            lnr_btn_plan2.setBackground(getResources().getDrawable(R.drawable.bg_btn_yellow));
        }

        /*if(!mSubscribed) {
            try {
                launchingGoogleWallet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            startActivity(new Intent(context, ActivityAlarmConfig.class));
        }*/

        //onPayButtonClicked();

        prepareForNextScreen();
        /*if (isPopupOpen) {
            prepareForNextScreen();
        } else
            openPopup(1);*/
    }

    boolean isPaymentApiSuccess = false;

    private void prepareForNextScreen() {
        if (mResultSku.contentEquals(Constants.SkuTrial)) {
            callSubscriptionApi();
            return;
        }

        if (!mSubscribed) {
            BillingFlowParams.Builder builder = BillingFlowParams.newBuilder()
                    .setSku(mResultSku).setType(BillingClient.SkuType.INAPP);
            int responseCode = mBillingClient.launchBillingFlow(this, builder.build());

            if (responseCode == 7) {
                callSubscriptionApi();
            }
        } else {
            if (mSubscribed && isPaymentApiSuccess)
                startActivity(new Intent(context, ActivityAlarmConfig.class));
            else
                callPaymentApi();
        }
    }

    private void ChangeColorOf(ViewGroup layout, int color) {
        TextView textView;
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view instanceof TextView) {
                textView = (TextView) view;
                textView.setTextColor(color);
            }
        }
    }

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
                    prepareForNextScreen();
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

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        //alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    void setWaitScreen(boolean set) {
        if (set)
            progressdialog.show();
        else
            progressdialog.dismiss();
    }

    @Override
    public void onBackPressed() {

    }


    private boolean mIsServiceConnected;

    public void getAllProductList() {
        List<String> skuList = new ArrayList<>();
        skuList.add(Constants.SkuMonth);
        skuList.add(Constants.SkuYear);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                        if (responseCode == BillingClient.BillingResponse.OK
                                && skuDetailsList != null) {
                            skuListDetail = skuDetailsList;
                            for (SkuDetails skuDetails : skuDetailsList) {
                                String sku = skuDetails.getSku();
                                String price = skuDetails.getPrice();

                                if (sku.equals(Constants.SkuMonth))
                                    ((TextView) findViewById(R.id.txt_price_month)).setText(price);
                                else if (sku.equals(Constants.SkuYear))
                                    ((TextView) findViewById(R.id.txt_price_year)).setText(price);
                            }
                        }
                        removeAlreadyPurchased();
                    }
                });
    }

    public void startServiceConnection() {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                Log.d(TAG, "Setup finished. Response code: " + billingResponseCode);

                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    mIsServiceConnected = true;
                    getAllProductList();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                mIsServiceConnected = false;
            }
        });
    }

    private void callSubscriptionApi() {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();

        final LoginModel userProfile = new Gson().fromJson(new Shared(context).getString(Shared.userProfile, ""), LoginModel.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("subscriptionType", mResultSku.split("_")[1]);
        jsonObject.addProperty("subscriptionByUserId", userProfile.getUserId());
        jsonObject.addProperty("subscriptionComplete", true);

        Api.getClient().AddSubscription(jsonObject, "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        GlobalVariable.getRegistrationModel().setSubscriptionComplete(true);
                        userProfile.setSubscriptioncomplete(true);
                        new Shared(context).putString(Shared.userProfile, new Gson().toJson(userProfile));
                    }
                } else {
                    //GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                progressDialog.dismiss();
                startActivity(new Intent(context, ActivityAlarmConfig.class));
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private String paidPrice, paidCurrency;

    private void callPaymentApi() {
        Log.e(TAG, paidPrice + "__" + paidCurrency);
        Log.e(TAG, paidPrice.substring(2, paidPrice.length()) + "__" + paidCurrency);

        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();

        final LoginModel userProfile = new Gson().fromJson(new Shared(context).getString(Shared.userProfile, ""), LoginModel.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("inAppPurchaseByUserId", userProfile.getUserId());
        jsonObject.addProperty("inAppPurchaseTotalAmount", 0);
        jsonObject.addProperty("inAppPurchaseOwnerAmount", 0);
        jsonObject.addProperty("inAppPurchaseUserAmount", 0);
        jsonObject.addProperty("inAppPurchaseUserLocalAmount", paidPrice.substring(2, paidPrice.length()));
        jsonObject.addProperty("inAppPurchaseUserLocalCurrencyType", paidCurrency);

        Api.getClient().AddInAppPurchase(jsonObject, "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
//                        removeAlreadyPurchased();
                        callSubscriptionApi();
                    }
                } else {
                    //GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == 0) {
            for (Purchase purchase : purchases) {
                if (purchase.getSku().contentEquals(mResultSku)) {
                    mSubscribed = true;
                    for (SkuDetails skuDetails : skuListDetail) {
                        String sku = skuDetails.getSku();
                        String price = skuDetails.getPrice();

                        if (sku.equals(mResultSku)) {
                            paidPrice = price;
                            paidCurrency = skuDetails.getPriceCurrencyCode();
                            callPaymentApi();
                        }
                    }
//                    mBillingClient.consumeAsync(purchase.getPurchaseToken(), listener);
                }
            }
        }
    }

    ConsumeResponseListener listener = new ConsumeResponseListener() {
        @Override
        public void onConsumeResponse(@BillingClient.BillingResponse int responseCode, String outToken) {
            if (responseCode == BillingClient.BillingResponse.OK) {
                Log.e(TAG, "Product Consume__" + mResultSku);
            }
        }
    };

    void removeAlreadyPurchased() {
        mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP,
                new PurchaseHistoryResponseListener() {
                    @Override
                    public void onPurchaseHistoryResponse(@BillingClient.BillingResponse int responseCode,
                                                          List<Purchase> purchasesList) {
                        if (responseCode == BillingClient.BillingResponse.OK && purchasesList != null) {
                            for (Purchase purchase : purchasesList) {
                                mBillingClient.consumeAsync(purchase.getPurchaseToken(), listener);
                            }
                        }
                    }
                });
    }
}
