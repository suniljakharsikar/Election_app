package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OurWorks extends AppCompatActivity {

    RecyclerView recyclerView;
    private ProgressBar avi;

    void startAnim() {
        //avi.show();
        avi.setVisibility(View.VISIBLE);
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.setVisibility(View.INVISIBLE);
//        avi.hide();
        // or avi.smoothToHide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_works);
        avi = findViewById(R.id.avi);

        recyclerView = findViewById(R.id.rv_our_works);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchData();

    }

    private void fetchData() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        String url = Url.baseurl + "/work_data";
        JSONObject jsonRequest = new JSONObject();
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ashok_ourwork", response.toString());
                try {
                    Gson gson = new Gson();
                    OurWorkResponseModel ow = gson.fromJson(response.toString(), OurWorkResponseModel.class);
                    if (ow.getSuccess()) {
                        RecyclerView.Adapter adapter = new OurWorkAdapter(ow.getData());
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(OurWorks.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OurWorks.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(OurWorks.this, LoginActivity.class));
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
                Utility.INSTANCE.customSnackBar(recyclerView,OurWorks.this,error.toString(),
                        ContextCompat.getColor(OurWorks.this,R.color.error),R.drawable.ic_error);                   }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("token", preferences.getString(Datas.token, ""));
                header.put("lid", preferences.getString(Datas.lagnuage_id, "1"));

                return header;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void back(View view) {
        finish();
    }
}