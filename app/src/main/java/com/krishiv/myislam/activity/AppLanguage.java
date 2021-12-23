package com.krishiv.myislam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.krishiv.myislam.R;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.LocaleHelper;

public class AppLanguage extends BaseMenuActivity {

    String[] strLanguage;
    String[] strLanguageExt;
    Spinner spnLanguage;
    String currentLanguage = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_app_language);

        strLanguage = getResources().getStringArray(R.array.languages);
        strLanguageExt = getResources().getStringArray(R.array.languages_ext);
        spnLanguage = findViewById(R.id.spn_language);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spineer_dropdown, strLanguage);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLanguage.setAdapter(dataAdapter);

        currentLanguage = LocaleHelper.getLanguage(context);

        for (int i=0; i<strLanguageExt.length; i++){
            if (strLanguageExt[i].contentEquals(currentLanguage)){
                spnLanguage.setSelection(i, false);
                break;
            }
        }

        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LocaleHelper.setLocale(context, strLanguageExt[spnLanguage.getSelectedItemPosition()]);
                Intent intent = new Intent(context, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
