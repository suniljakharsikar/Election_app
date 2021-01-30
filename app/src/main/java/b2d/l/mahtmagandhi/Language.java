package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Language extends AppCompatActivity {

    ListView listView;
    private SharedPreferences preferences;
    private MyLangAdapter listadapter;
    private ProgressBar avi;

    void startAnim() {
       // avi.show();
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
        boolean loginstatus = preferences.getBoolean(Datas.language_selected, false);
        if (loginstatus) {
            startActivity(new Intent(Language.this, Home.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        avi = findViewById(R.id.avi);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
//        hideSystemUI();
        listView = findViewById(R.id.language_list);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(Language.this, "test", // Toast.LENGTH_SHORT).show();
            }
        });*/

        stopAnim();
        String url = Url.baseurl + "/language_list";
        JSONObject jsonRequest = new JSONObject();
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        JSONArray data = response.getJSONArray("data");
                        String[] strings = new String[data.length()];
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
//                            JsonObject jsonObject = (JsonObject) data.get(i);
                            strings[i] = String.valueOf(jsonObject.get("name"));

                        }
                        listadapter = new MyLangAdapter(Language.this, strings);
                        listView.setAdapter(listadapter);
                    } else {
                        // Toast.makeText(Language.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Language.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(Language.this, LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                stopAnim();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utility.INSTANCE.customSnackBar(listView,Language.this,error.toString(),
                        ContextCompat.getColor(Language.this,R.color.error),R.drawable.ic_error);
                stopAnim();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Token", preferences.getString(Datas.token, ""));
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void back(View view) {
        finish();

    }

    public void submit(View view) {
       /* String index = String.valueOf(listadapter.getselected() + 1);
        String url = Url.baseurl + "/language_update'";
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("langId", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    // Toast.makeText(Language.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    if (success) {
                        startActivity(new Intent(Language.this, Home.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(Language.this, "" + error, // Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Token", preferences.getString(Datas.token, ""));
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);*/


        String url = Url.baseurl + "/language_update";
        JSONObject jsonRequest = new JSONObject();
        String index = String.valueOf(listadapter.getselected() + 1);
        try {
            jsonRequest.put("langId", index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Language.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(Datas.language_selected, true);
                        editor.putString(Datas.lagnuage_id, index);
                        editor.apply();
                        startActivity(new Intent(Language.this, Home.class));
                        finish();

                    } else {
                        // Toast.makeText(Language.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Language.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(Language.this, LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                stopAnim();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                stopAnim();
                // Toast.makeText(Language.this, "" + error, // Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("token", preferences.getString(Datas.token, ""));
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }
}