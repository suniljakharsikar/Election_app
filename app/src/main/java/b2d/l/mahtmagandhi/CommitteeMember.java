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
import android.widget.ProgressBar;

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

public class CommitteeMember extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<CommitteeMemberData> committeeMemberData;
    private SharedPreferences preferences;
    private ProgressBar avi;

    void startAnim() {
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
        setContentView(R.layout.activity_committee_member);
        avi = findViewById(R.id.avi);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        recyclerView = findViewById(R.id.rv4);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
//        committeeMemberData.add(new CommitteeMemberData("Vinita Kumari", "Sarpanch Jhotwara", R.drawable.pro1));
//        committeeMemberData.add(new CommitteeMemberData("Ravindra Kumar", "Advocate", R.drawable.pro2));
//        committeeMemberData.add(new CommitteeMemberData("Dr. Vinay", "Doctor Child Speciality", R.drawable.pro3));
//        CommitteeMemberAdapter committeeMemberAdapter = new CommitteeMemberAdapter(this, committeeMemberData);
//        recyclerView.setAdapter(committeeMemberAdapter);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String url = Url.baseurl + "/committee_data";
        JSONObject json = new JSONObject();
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ashok_comm", response.toString());

                try {
                    if (response.getBoolean("success")) {
                       /* JSONArray data = response.getJSONArray("data");
                        committeeMemberData = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);

                            CommitteeMemberData c = gson.fromJson(jsonObject.toString(), CommitteeMemberData.class);
                            committeeMemberData.add(c);
                        }*/
                        Gson gson = new Gson();
                        CommitteMembersResponseModel c = gson.fromJson(response.toString(), CommitteMembersResponseModel.class);

                        CommitteeMemberAdapter committeeMemberAdapter1 = new CommitteeMemberAdapter(CommitteeMember.this, c.getData(), c.getDomain_name());
                        recyclerView.setAdapter(committeeMemberAdapter1);
                    } else {
                        // // // Toast.makeText(CommitteeMember.this, "" + response.getString("message"), // // // Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CommitteeMember.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(CommitteeMember.this, LoginActivity.class));
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
                Utility.INSTANCE.customSnackBar(recyclerView,CommitteeMember.this,error.toString(),
                        ContextCompat.getColor(CommitteeMember.this,R.color.error),R.drawable.ic_error);
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