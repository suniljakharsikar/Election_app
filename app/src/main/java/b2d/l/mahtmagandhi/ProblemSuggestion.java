package b2d.l.mahtmagandhi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ProblemSuggestion extends AppCompatActivity {

    private RecyclerView rvProblems;
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
        setContentView(R.layout.activity_problem_suggestion2);
        avi = findViewById(R.id.avi);

        initView();
    }

    private void initView() {
        rvProblems = findViewById(R.id.rv_probs);
        rvProblems.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
    }

    public void back(View view) {
        finish();
    }

    public void createNewProbSug(View view) {
        startActivity(new Intent(this,CreateProblemAndSuggestionActivity.class));
    }
    private void fetchData() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        String url = Url.baseurl + "/psuggestion_data";
        JSONObject jsonRequest = new JSONObject();
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ashok_problem", response.toString());
                try {
                    Gson gson = new Gson();
                    ProblemsResponseModel ow = gson.fromJson(response.toString(), ProblemsResponseModel.class);
                    if (ow.getSuccess()) {
                        rvProblems.setAdapter(new ProblemSuggestionRecyclerViewAdapter(ow.getData(),"/psuggestion_comment_list","/psuggestion_comment_post"));
                    } else {
                        // Toast.makeText(ProblemSuggestion.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProblemSuggestion.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(ProblemSuggestion.this, LoginActivity.class));
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
                Utility.INSTANCE.customSnackBar(rvProblems,ProblemSuggestion.this,error.toString(),
                        ContextCompat.getColor(ProblemSuggestion.this,R.color.error),R.drawable.ic_error);                   }
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

}