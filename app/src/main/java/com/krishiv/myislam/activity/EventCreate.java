package com.krishiv.myislam.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.BuildConfig;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.EventModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.GlobalTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCreate extends BaseMenuActivity implements View.OnClickListener {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 23487;
    Spinner spn_event_category;
    EditText edt_event_name, edt_event_type, edt_event_mobile_number, edt_event_description;
    TextView edt_event_from, edt_event_to, edt_event_address,edt_event_country,edt_event_city;
    EventModel eventModel;
    String strTime = ""; //"2019-03-06T05:39:54.523Z"
    private PlacesClient placesClient;
    private boolean isUpdate = false;
    String event_date_from,event_date_to;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_create_event);
        Places.initialize(getApplicationContext(), BuildConfig.places_api_key);
        placesClient = Places.createClient(this);


        spn_event_category = findViewById(R.id.spn_event_category);
        edt_event_name = findViewById(R.id.edt_event_name);
        edt_event_type = findViewById(R.id.edt_event_type);
        edt_event_address = findViewById(R.id.edt_event_address);
        edt_event_country = findViewById(R.id.edt_event_country);
        edt_event_city = findViewById(R.id.edt_event_city);
        edt_event_mobile_number = findViewById(R.id.edt_event_mobile_number);
        edt_event_description = findViewById(R.id.edt_event_description);
        edt_event_from = findViewById(R.id.edt_event_from);
        edt_event_to = findViewById(R.id.edt_event_to);

        edt_event_address.setOnClickListener(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.event_category));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_event_category.setAdapter(dataAdapter);

        eventModel = new EventModel();

        try {
            eventModel = new Gson().fromJson(getIntent().getStringExtra("data"), EventModel.class);

            edt_event_name.setText(eventModel.getMyEventName());
            edt_event_address.setText(eventModel.getAddress());
            edt_event_country.setText(eventModel.getCountry());
            edt_event_city.setText(eventModel.getCity());
            edt_event_mobile_number.setText(eventModel.getMobileNumber());
            edt_event_description.setText(eventModel.getDescription());
            edt_event_from.setText(eventModel.getMyEventStartDate());
            edt_event_to.setText(eventModel.getMyEventEndDate());

            ((Button) findViewById(R.id.btn_save)).setText("Update Event");

            isUpdate = true;

        } catch (Exception e) {
            eventModel = new EventModel();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.edt_event_from:
                openDatePiker(1);
                break;
            case R.id.edt_event_to:
                openDatePiker(2);
                break;
            case R.id.btn_save:
                if (isValid()) {
                    readyForCallApi();
                }
                break;
            case R.id.edt_event_address:
                startAutocompleteActivity();

        }
    }

    private void readyForCallApi() {



        eventModel.setMyEventName(edt_event_name.getText().toString().trim());
        eventModel.setMyEventCategory(spn_event_category.getSelectedItemPosition() + 1);
        eventModel.setAddress(edt_event_address.getText().toString().trim());
        eventModel.setCountry(edt_event_country.getText().toString().trim());
        eventModel.setCity(edt_event_city.getText().toString().trim());

        eventModel.setMobileNumber(edt_event_mobile_number.getText().toString().trim());
        eventModel.setDescription(edt_event_description.getText().toString().trim());
        eventModel.setMyEventStartDate(event_date_from+ ".000Z");
        eventModel.setMyEventEndDate(event_date_to+ ".000Z");

        eventModel.setMyEventLatitude(String.valueOf(latitude));
        eventModel.setMyEventLongitude(String.valueOf(longitude));
        eventModel.setMyEventMinor(true);
        eventModel.setDistance("0");

        if (!isUpdate)
            callApi();
        else
            callUpdateApi();
    }

    private boolean isValid() {
        return true;
    }

    private void callUpdateApi() {
        GlobalTask.showProgressDialog(this);

        Api.getClient().UpdateEvent(eventModel, "Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(EventCreate.this, resultObj.getContent().toString());
                        finish();
                    }
                } else {
                    //GlobalTask.showTostErrorResponse(EventCreate.this, response.errorBody());
                }
                GlobalTask.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                GlobalTask.hideProgressDialog();
            }
        });
    }

    private void callApi() {
        GlobalTask.showProgressDialog(this);

        Api.getClient().AddEvent(eventModel, "Bearer " + BaseMenuActivity.userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(EventCreate.this, resultObj.getContent().toString());
                        finish();
                    }
                } else {
                    //GlobalTask.showTostErrorResponse(EventCreate.this, response.errorBody());
                }
                GlobalTask.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                GlobalTask.hideProgressDialog();
            }
        });
    }

    private void openDatePiker(final int actionFor) {
        final Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(EventCreate.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        monthOfYear = monthOfYear + 1;
                        String strD = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
                        String strM = monthOfYear < 10 ? "0" + monthOfYear : monthOfYear + "";

                        strTime = year + "-" + strM + "-" + strD;
                        openTimePiker(actionFor);

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void openTimePiker(final int actionFor) {
        final Calendar c = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(EventCreate.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        String strH = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                        String strM = minute < 10 ? "0" + minute : minute + "";

                        if (actionFor == 1){
                            if (hourOfDay < 12 && hourOfDay >= 0) {
                                edt_event_from.setText(strTime + " " + strH + ":" + strM +":00" +"AM");
                                String s = edt_event_from.getText().toString();
                                String[] arr = s.split("AM");
                                for ( String ss : arr) {
                                    event_date_from=ss;
                                }
                            } else {
                                hourOfDay -= 12;
                                if(hourOfDay == 0) {
                                    hourOfDay = 12;
                                }
                                edt_event_from.setText(strTime + " " + strH + ":" + strM +":00"+"PM");
                                String s = edt_event_from.getText().toString();
                                String[] arr = s.split("PM");
                                for ( String ss : arr) {
                                    event_date_from=ss;
                                }
                            }

                        }

                        else
                        {
                            if (hourOfDay < 12 && hourOfDay >= 0) {
                                edt_event_to.setText(strTime + " " + strH + ":" + strM +":00" +"AM");
                                String s = edt_event_to.getText().toString();
                                String[] arr = s.split("AM");
                                for ( String ss : arr) {
                                    event_date_to=ss;
                                }
                            } else {
                                hourOfDay -= 12;
                                if(hourOfDay == 0) {
                                    hourOfDay = 12;
                                }
                                edt_event_to.setText(strTime + " " + strH + ":" + strM +":00" +"PM");
                                String s = edt_event_to.getText().toString();
                                String[] arr = s.split("PM");
                                for ( String ss : arr) {
                                    event_date_to=ss;
                                }
                            }
                        }

                        strTime = "";
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }


    private void startAutocompleteActivity() {
        Intent autocompleteIntent =
                new Autocomplete.IntentBuilder(getMode(), getPlaceFields())
                        // .setCountry(getCountry())
                        .build(EventCreate.this);
        startActivityForResult(autocompleteIntent, AUTOCOMPLETE_REQUEST_CODE);
    }

    private List<Place.Field> getPlaceFields() {

        List<Place.Field> list = new ArrayList<>();
        list.add(Place.Field.ADDRESS);
        list.add(Place.Field.LAT_LNG);
        return list;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == AutocompleteActivity.RESULT_OK) {

                try {
                    Place place = Autocomplete.getPlaceFromIntent(intent);
                    edt_event_address.setText(place.getAddress());
                    LatLng coordinates = place.getLatLng(); // Get the coordinates from your place
                    latitude=place.getLatLng().latitude;
                    longitude=place.getLatLng().longitude;
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());

                    List<Address> addresses = geocoder.getFromLocation(
                            coordinates.latitude,
                            coordinates.longitude,
                            1); // Only retrieve 1 address
                    Address address = addresses.get(0);


//                    edt_event_address.setText(address.getAddressLine(0));
                    edt_event_country.setText(address.getCountryName());
                    edt_event_city.setText(address.getLocality());



                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("country" + getPlaceFields());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(intent);
                Log.e("Location", status.getStatusMessage());

            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        // Required because EventCreate.this class extends AppCompatActivity which extends FragmentActivity
        // which implements EventCreate.this method to pass onActivityResult calls to child fragments
        // (eg AutocompleteFragment).
        super.onActivityResult(requestCode, resultCode, intent);
    }


    private String getCountry() {
        return ((TextView) findViewById(R.id.edt_event_country)).getText().toString();
    }

    private AutocompleteActivityMode getMode() {

        return AutocompleteActivityMode.FULLSCREEN;
    }

}
