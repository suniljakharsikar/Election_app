package b2d.l.mahtmagandhi;

import androidx.annotation.Nullable;
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
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommunityChat extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ChatData> chatData;
    private SharedPreferences preferences;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_chat);
        avi = findViewById(R.id.avi);

        stopAnim();
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(CommunityChat.this, NewPost.class));
                startActivityForResult(new Intent(CommunityChat.this, NewPost.class), 1010);
            }
        });
        recyclerView = findViewById(R.id.rv4);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setItemViewCacheSize(50);
        int itemViewType = 0;
        recyclerView.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 1010 & data.getBooleanExtra("reload", false)) {
  Utility.INSTANCE.customSnackBar(recyclerView, CommunityChat.this,"Successfully Submitted" , ContextCompat.getColor(CommunityChat.this, R.color.success), R.drawable.ic_success);
            reload();
            // // Toast.makeText(this, "runnind code", // Toast.LENGTH_SHORT).show();
        }
    }


    private void reload() {
        startAnim();
        String url = Url.baseurl + "/ctalk_data";
        JSONObject json = new JSONObject();
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //stopAnim();
                Log.d("ashok_chat", response.toString());

                try {
                    Gson gson = new Gson();
                    ChatDataResponseModel cm = gson.fromJson(response.toString(), ChatDataResponseModel.class);
                    if (cm.getSuccess()) {
                       /* JSONArray data = response.getJSONArray("data");
                        chatData = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);

                            ChatData c = gson.fromJson(jsonObject.toString(), ChatData.class);
                            chatData.add(c);
                        }*/

                        CommunityChatAdapter communityChatAdapter = new CommunityChatAdapter(CommunityChat.this, cm.getData(), "/ctalk_like_unlike_post", "/ctalk_comments", "/ctalk_comments_post", false, avi);
                        recyclerView.setAdapter(communityChatAdapter);
                    } else {
                       // if (Datas.DEBUG)
                        // Toast.makeText(CommunityChat.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                        //login page

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CommunityChat.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(CommunityChat.this, LoginActivity.class));
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
                if (Datas.DEBUG)
                // Toast.makeText(CommunityChat.this, "" + error.toString(), // Toast.LENGTH_SHORT).show();

                Utility.INSTANCE.customSnackBar(recyclerView,CommunityChat.this,error.toString(), ContextCompat.getColor(CommunityChat.this,R.color.error),R.drawable.ic_error);
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