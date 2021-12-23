package com.krishiv.myislam.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.ChangePwdModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.utils.GlobalTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePass extends BaseMenuActivity {

    EditText edt_opwd, edt_npwd, edt_cpwd;
    Button btn_save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = setContentViev(R.layout.fg_change_pwd);

        edt_opwd = view.findViewById(R.id.edt_opwd);
        edt_npwd = view.findViewById(R.id.edt_npwd);
        edt_cpwd = view.findViewById(R.id.edt_cpwd);
        btn_save = view.findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()) {
                    ChangePwdModel changePwdModel = new ChangePwdModel();
                    changePwdModel.setOldPassword(edt_opwd.getText().toString().trim().toString());
                    changePwdModel.setNewPassword(edt_npwd.getText().toString().trim().toString());
                    changePwdModel.setConfirmPassword(edt_cpwd.getText().toString().trim().toString());
                    callApi(changePwdModel);
                }
            }
        });

        view.findViewById(R.id.arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void callApi(ChangePwdModel jsonObject) {
        GlobalTask.showProgressDialog(context);

        Api.getClient().ChangePassword(jsonObject,  "Bearer "+userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        GlobalTask.showToastMessage(context, resultObj.getContent().toString());
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

    private boolean isValid() {
        return true;
    }
}

