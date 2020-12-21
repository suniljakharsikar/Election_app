package b2d.l.mahtmagandhi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.gloxey.gnm.interfaces.VolleyResponse;
import io.gloxey.gnm.managers.ConnectionManager;

import static android.media.CamcorderProfile.get;

public class Register extends AppCompatActivity {

    EditText name, number, age, pincode, stateEt, distirctEt;
    Spinner cityEt;
    private SharedPreferences preferences;
    private AVLoadingIndicatorView avi;

    void startAnim() {
        avi.show();
        avi.setVisibility(View.VISIBLE);
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.setVisibility(View.INVISIBLE);
//        avi.hide();
        // or avi.smoothToHide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loginstatus = preferences.getBoolean(Datas.registration, false);
        if (loginstatus) {
            startActivity(new Intent(Register.this, Language.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        avi = findViewById(R.id.avi3);


        name = findViewById(R.id.editTextPersonName);
        number = findViewById(R.id.editTextPhone);
        age = findViewById(R.id.editText_dob);
        age.setInputType(InputType.TYPE_NULL);
        age.setFocusable(false);
        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialDatePicker<Long> dp = MaterialDatePicker.Builder.datePicker().build();
                dp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long it) {
                        Date date =new  Date(it);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                        //formatter.setTimeZone(TimeZone.getTimeZone(""))
                        String dateFormatted = formatter.format(date);
                        age.setText(dateFormatted);
                    }
                }) ;
                dp.show(getSupportFragmentManager(), "date");
            }
        });
        pincode = findViewById(R.id.editText_postal_code);
        stateEt = findViewById(R.id.editText_state);
        distirctEt = findViewById(R.id.editText_district);
        cityEt = findViewById(R.id.spinner_city_regi);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        name.setText(preferences.getString(Datas.user_name, ""));
        number.setText(preferences.getString(Datas.user_mobile, ""));
        age.setText(preferences.getString(Datas.user_age, ""));
        pincode.setText(preferences.getString(Datas.user_postal_code, ""));
        stateEt.setText(preferences.getString(Datas.user_state, ""));
        distirctEt.setText(preferences.getString(Datas.user_district, ""));
//        city.setText(preferences.getString(Datas.user_district, ""));

        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>5 && s.length()==6)
                fetchLoc(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>5 && s.length()==6)
                    fetchLoc(s.toString());
            }
        });
        stopAnim();
    }

    private void fetchLoc(String s) {


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, "https://api.postalpincode.in/pincode/" + s,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register", "onResponse: "+response);

                Gson gson = new Gson();
               PinCodeResponseModel pinCodeResponseModelItems =   gson.fromJson(response.toString(),PinCodeResponseModel.class);
                try {
                    if (pinCodeResponseModelItems!=null && pinCodeResponseModelItems.size()>0){
                        PinCodeResponseModelItem pinItem = pinCodeResponseModelItems.get(0);
                        List<PinCodeResponseModelItem.PostOffice> postOffice = pinItem.getPostOffice();
                        PinCodeResponseModelItem.PostOffice post = postOffice.get(0);
                        String state = post.getState();
                        stateEt.setText(state);

                        String district = post.getDistrict();

                        distirctEt.setText(district);
                        List<String>  cities = new ArrayList<>();
                        for(PinCodeResponseModelItem.PostOffice postObj :postOffice){
                            cities.add(postObj.getName());
                        }
                        cityEt.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line,cities));
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Register", "onErrorResponse: "+error);

            }
        });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }

    public void back(View view) {
        finish();
    }

    public void submit(View view) {
        call();
//        call1();
    }

    private void call1() {
        String requestURL = Url.baseurl + "/update_profile";

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Token", preferences.getString(Datas.token, ""));

        JSONObject jsonRwquest = new JSONObject();
        try {
            jsonRwquest.put("userMobile", number.getText().toString());
            jsonRwquest.put("userName", name.getText().toString());
            int x = Integer.parseInt(age.getText().toString());
            jsonRwquest.put("userAge", x);
            jsonRwquest.put("userPostalCode", pincode.getText().toString());
            jsonRwquest.put("userState", stateEt.getText().toString());
            jsonRwquest.put("userDistrict", distirctEt.getText().toString());
            jsonRwquest.put("userVillage", cityEt.getSelectedItem().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ConnectionManager.volleyJSONRequest(this, true, null, requestURL, Request.Method.POST, jsonRwquest, headers, new VolleyResponse() {
            @Override
            public void onResponse(String _response) {

//                _response.
                Toast.makeText(Register.this, "" + _response, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Register.this, "" + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void isNetwork(boolean connected) {

//                Toast.makeText(Register.this, "" + connected, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void call() {

        String url = Url.baseurl + "/update_profile";
        JSONObject jsonRwquest = new JSONObject();
        String cityS = "";
        if (cityEt.getSelectedItem()==null)cityS="";else cityS = cityEt.getSelectedItem().toString();
        try {
            jsonRwquest.put("userMobile", number.getText().toString());
            jsonRwquest.put("userName", name.getText().toString());
            int x = Integer.parseInt(age.getText().toString());
            jsonRwquest.put("userAge", x);
            jsonRwquest.put("userPostalCode", pincode.getText().toString());
            jsonRwquest.put("userState", stateEt.getText().toString());
            jsonRwquest.put("userDistrict", distirctEt.getText().toString());
            jsonRwquest.put("userVillage",cityS );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRwquest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        Toast.makeText(Register.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Register.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(Datas.registration, true);
                        editor.apply();
                        startActivity(new Intent(Register.this, Language.class));
                        finish();

                    } else {
                        Toast.makeText(Register.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                stopAnim();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                stopAnim();
                Toast.makeText(Register.this, "e= " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Token", preferences.getString(Datas.token, ""));
                return headers;
            }

           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> x = new HashMap<>();
                x.put("userMobile", number.getText().toString());
                x.put("userName", name.getText().toString());
                x.put("userAge", age.getText().toString());
                x.put("userPostalCode", pincode.getText().toString());
                x.put("userState", state.getText().toString());
                x.put("userDistrict", distirct.getText().toString());
                return x;
            }*/
        }
                /*{
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", preferences.getString(Datas.token, ""));
                return params;

               *//* Map<String, String> params = new HashMap<String, String>();
                params.put("'Content-Type", "application/json");
                params.put("'Token", preferences.getString(Datas.token, ""));
                Log.d("ashok", "get header call");

                return params;*//*
            }
        }*/;
//        requestQueue.add(jsonObjectRequest);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}