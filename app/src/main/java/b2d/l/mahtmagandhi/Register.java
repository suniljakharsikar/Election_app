package b2d.l.mahtmagandhi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText name, number, age, pincode, state, distirct;
    Spinner city;
    private SharedPreferences preferences;
    private AVLoadingIndicatorView avi;
    private Calendar myCalendar;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        avi = findViewById(R.id.avi3);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        name = findViewById(R.id.editTextPersonName);
        number = findViewById(R.id.editTextPhone);
        age = findViewById(R.id.editText_dob);
        pincode = findViewById(R.id.editText_postal_code);
        state = findViewById(R.id.editText_state);
        distirct = findViewById(R.id.editText_district);
        city = findViewById(R.id.spinner_city_regi);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        name.setText(preferences.getString(Datas.user_name, ""));
        number.setText(preferences.getString(Datas.user_mobile, ""));
        age.setText(preferences.getString(Datas.user_age, ""));
        pincode.setText(preferences.getString(Datas.user_postal_code, ""));
        state.setText(preferences.getString(Datas.user_state, ""));
        distirct.setText(preferences.getString(Datas.user_district, ""));
//        city.setText(preferences.getString(Datas.user_district, ""));
        stopAnim();

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        /*age.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Register.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        age.setText(sdf.format(myCalendar.getTime()));
    }

    public void back(View view) {
        finish();
    }

    public void submit(View view) {
        call();
//        call1();
    }

    /*private void call1() {
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
            jsonRwquest.put("userState", state.getText().toString());
            jsonRwquest.put("userDistrict", distirct.getText().toString());
            jsonRwquest.put("userVillage", city.getSelectedItem().toString());

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

    }*/
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private void call() {

        String s = name.getText().toString();
        if (isNullOrEmpty(s)) {
            Toast.makeText(this, "Please type name", Toast.LENGTH_SHORT).show();
            return;
        }
        String s5 = age.getText().toString();
        if (isNullOrEmpty(s5)) {
            Toast.makeText(this, "Please type your age", Toast.LENGTH_SHORT).show();
            return;
        }
        String s1 = pincode.getText().toString();
        if (isNullOrEmpty(s1)) {
            Toast.makeText(this, "Please type pincode", Toast.LENGTH_SHORT).show();
            return;
        }
        String s3 = state.getText().toString();
        if (isNullOrEmpty(s3)) {
            Toast.makeText(this, "Please type state name", Toast.LENGTH_SHORT).show();
            return;
        }
        String s4 = distirct.getText().toString();
        if (isNullOrEmpty(s4)) {
            Toast.makeText(this, "Please type district name", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Url.baseurl + "/update_profile";
        JSONObject jsonRwquest = new JSONObject();
        String cityS;
        if (city.getSelectedItem() == null) cityS = "";
        else cityS = city.getSelectedItem().toString();
        try {
            jsonRwquest.put("userMobile", number.getText().toString());
            jsonRwquest.put("userName", s);
            int x = Integer.parseInt(s5);
            jsonRwquest.put("userAge", x);
            jsonRwquest.put("userPostalCode", s1);
            jsonRwquest.put("userState", s3);
            jsonRwquest.put("userDistrict", s4);
            jsonRwquest.put("userVillage", cityS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRwquest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    Toast.makeText(Register.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                    if (success) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Register.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(Datas.registration, true);
                        editor.apply();
                        startActivity(new Intent(Register.this, Language.class));
                        finish();

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
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
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