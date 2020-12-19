package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Survey extends AppCompatActivity {
    RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        recyclerView = findViewById(R.id.rv4);
// use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        fetchData();


    }

    private void fetchData() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        String url = Url.baseurl + "/survey_questions";
        JSONObject jsonRequest = new JSONObject();
        //startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Gson gson = new Gson();
                    SurveyResponseModel s = gson.fromJson(response.toString(), SurveyResponseModel.class);
                    if (s.getSuccess()) {

                        RecyclerView.Adapter adapter = new Surveydapter( s.getData());
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(Survey.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Survey.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(Survey.this, LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //stopAnim();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //stopAnim();
                Toast.makeText(Survey.this, "" + error, Toast.LENGTH_SHORT).show();
            }
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