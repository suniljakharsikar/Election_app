package b2d.l.mahtmagandhi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.gloxey.gnm.interfaces.VolleyResponse;
import io.gloxey.gnm.managers.ConnectionManager;

public class Register extends AppCompatActivity {

    EditText name, number, age, pincode, state, distirct, city, gender;
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
        avi = findViewById(R.id.avi);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        name = findViewById(R.id.editTextTextPersonName);
        number = findViewById(R.id.editTextPhone);
        age = findViewById(R.id.editTextNumber);
        pincode = findViewById(R.id.editTextNumber2);
        state = findViewById(R.id.editTextTextPersonName3);
        distirct = findViewById(R.id.editTextTextPersonName4);
        city = findViewById(R.id.editTextTextPersonName6);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        name.setText(preferences.getString(Datas.user_name, ""));
        number.setText(preferences.getString(Datas.user_mobile, ""));
        age.setText(preferences.getString(Datas.user_age, ""));
        pincode.setText(preferences.getString(Datas.user_postal_code, ""));
        state.setText(preferences.getString(Datas.user_state, ""));
        distirct.setText(preferences.getString(Datas.user_district, ""));
//        city.setText(preferences.getString(Datas.user_district, ""));
        stopAnim();
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
            jsonRwquest.put("userState", state.getText().toString());
            jsonRwquest.put("userDistrict", distirct.getText().toString());
            jsonRwquest.put("userVillage", city.getText().toString());

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
        try {
            jsonRwquest.put("userMobile", number.getText().toString());
            jsonRwquest.put("userName", name.getText().toString());
            int x = Integer.parseInt(age.getText().toString());
            jsonRwquest.put("userAge", x);
            jsonRwquest.put("userPostalCode", pincode.getText().toString());
            jsonRwquest.put("userState", state.getText().toString());
            jsonRwquest.put("userDistrict", distirct.getText().toString());
            jsonRwquest.put("userVillage", city.getText().toString());

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