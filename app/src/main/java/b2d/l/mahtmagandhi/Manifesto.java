package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Manifesto extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manifesto);
        avi = findViewById(R.id.avi);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        recyclerView = findViewById(R.id.rv3);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        fetchData();

    }

    private void fetchData() {
        startAnim();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        String url = Url.baseurl + "/manifesto";
        JSONObject jsonRequest = new JSONObject();
        //startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ashok_mani", response.toString());
                try {
                    Gson gson = new Gson();
                    ManifestoResponseModel m = gson.fromJson(response.toString(), ManifestoResponseModel.class);
                    if (m.getSuccess()) {

                        RecyclerView.Adapter adapter = new ManifestoAdapter(m.getData());
                        recyclerView.setAdapter(adapter);
                    } else {
                        // Toast.makeText(Manifesto.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Manifesto.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(Manifesto.this, LoginActivity.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stopAnim();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                stopAnim();
                Utility.INSTANCE.customSnackBar(recyclerView,Manifesto.this,error.toString(),
                        ContextCompat.getColor(Manifesto.this,R.color.error),R.drawable.ic_error);            }
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