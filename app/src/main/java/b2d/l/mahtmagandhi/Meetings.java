package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ConcatAdapter;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Meetings extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences preferences;
    private AVLoadingIndicatorView avi;
    private TextView timeHeading;

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
        setContentView(R.layout.activity_meetings);
        avi = findViewById(R.id.avi);
        timeHeading = findViewById(R.id.textView_month_year_meeting);

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        recyclerView = findViewById(R.id.rv1);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        int[] x = {1, 2, 3, 3, 3};

        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        String url = Url.baseurl + "/meeting_data";
        JSONObject jsonRequest = new JSONObject();
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ashok_meeting", response.toString());
                try {
                    if (response.getBoolean("success")) {
                        JSONArray data = response.getJSONArray("data");
                        ArrayList<Meeting> meetings = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            meetings.add(new Meeting(jsonObject.getString("id"), jsonObject.getString("title"), jsonObject.getString("description"), jsonObject.getString("meeting_date"), jsonObject.getString("meeting_time"),jsonObject.getString("latitude"),jsonObject.getString("longitude")));
                        }
                        Map<String, List<Meeting>> map = new HashMap<>();
                        for (int i = 0; i < meetings.size(); i++) {
                            Meeting item = meetings.get(i);

                            List<Meeting> ms = new ArrayList<>();
                            if (map.containsKey(item.getMeeting_date())) {
                                List<Meeting> ts = map.get(item.getMeeting_date());
                                ts.add(item);
                                map.put(item.getMeeting_date(), ts);
                            } else {
                                ms.add(item);
                                map.put(item.getMeeting_date(), ms);
                            }
                        }
                        Log.d("Meetings", "onResponse: " + map.toString());
                        Set<String> keys = map.keySet();
                        ConcatAdapter concatAdapter = new ConcatAdapter();
                        for (String key : keys) {
                            concatAdapter.addAdapter(new MeetingHeaderAdapter(key,timeHeading));
                            List<Meeting> v = map.get(key);
                            concatAdapter.addAdapter(new MeetingAdapter(v, Meetings.this, recyclerView));
                        }
                        recyclerView.setAdapter(concatAdapter);
                    } else {
                        // Toast.makeText(Meetings.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Meetings.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(Meetings.this, LoginActivity.class));
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
                Utility.INSTANCE.customSnackBar(recyclerView,Meetings.this,error.toString(),
                        ContextCompat.getColor(Meetings.this,R.color.error),R.drawable.ic_error);            }
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