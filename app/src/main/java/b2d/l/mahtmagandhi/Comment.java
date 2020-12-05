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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Comment extends AppCompatActivity {

    RecyclerView recyclerView;
    private SharedPreferences preferences;
    EditText comment;
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
        setContentView(R.layout.activity_comment);
        StatusBarUtil.setTransparent(this);
        avi = findViewById(R.id.avi);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        recyclerView = findViewById(R.id.rv_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        comment = findViewById(R.id.editTextTextMultiLine2);
        /*ArrayList<CommentData> data = new ArrayList<>();
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet, "));
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet, "));
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet, "));
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet, , "));
        RecyclerView.Adapter adapter = new CommentAdapter(this, data);
        recyclerView.setAdapter(adapter);*/
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String url = Url.baseurl + "/ctalk_comments";
        JSONObject json = new JSONObject();
        try {
            json.put("parentId", getIntent().getStringExtra("pos"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        ArrayList<CommentData> commentData = new ArrayList<>();
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            Gson gson = new Gson();
                            CommentData comment = gson.fromJson(data.getJSONObject(i).toString(), CommentData.class);
                            commentData.add(comment);
                        }
                        RecyclerView.Adapter adapter = new CommentAdapter(Comment.this, commentData);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(Comment.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Comment.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(Comment.this, LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                stopAnim();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comment.this, "" + error, Toast.LENGTH_SHORT).show();
                stopAnim();
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

    public void newpost(View view) {
        String url = Url.baseurl + "/ctalk_comments_post";
        JSONObject json = new JSONObject();
        try {
            json.put("parentId", getIntent().getStringExtra("pos"));
            json.put("comment", comment.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        JSONArray data = response.getJSONArray("data");
                        Gson gson = new Gson();
                        ArrayList<CommentData> commentData = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            CommentData comment = gson.fromJson(data.getJSONObject(i).toString(), CommentData.class);
                            commentData.add(comment);
                        }
                        RecyclerView.Adapter adapter = new CommentAdapter(Comment.this, commentData);
                        recyclerView.setAdapter(adapter);
                        comment.setText("");
                    }
                    Toast.makeText(Comment.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                stopAnim();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopAnim();
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
}