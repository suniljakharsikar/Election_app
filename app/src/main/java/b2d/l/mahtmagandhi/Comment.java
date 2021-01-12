package b2d.l.mahtmagandhi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
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
    private Boolean passToken;
    TextView textView;
    MaterialCardView materialCardView;
    CardView comment_box;
    private boolean resol;
    private MaterialCardView materialCardViewResolved;

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
        final SliderView sliderView = findViewById(R.id.imageSlider);
        textView = findViewById(R.id.textView30);
        comment_box = findViewById(R.id.cardView2);

        final SliderAdapterExample adapter = new SliderAdapterExample(this);
//        StatusBarUtil.setTransparent(this);
        avi = findViewById(R.id.avi);
        materialCardViewResolved = findViewById(R.id.mcv_comment_resolved);

        if (getIntent() != null && getIntent().hasExtra("isResolved")) {
            resol = getIntent().getBooleanExtra("isResolved", false);
            resolwork();
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        recyclerView = findViewById(R.id.rv_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        comment = findViewById(R.id.editTextTextMultiLine2);
        /*ArrayList<CommentData> data = new ArrayList<>();
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet, "));
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet, "));
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet, "));
        data.add(new CommentData("Pankaj Choudhary ", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet, , "));
        RecyclerView.Adapter adapter = new CommentAdapter(this, data);
        recyclerView.setAdapter(adapter);*/
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        passToken = getIntent().getBooleanExtra("passToken", true);

        materialCardView = findViewById(R.id.mcv_comment_resolved);

        if (passToken) {//from problem page
            sliderView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);

//            Toast.makeText(this, "comming from problem", Toast.LENGTH_SHORT).show();
            try {
                ArrayList<ProblemsResponseModel.Data.ImageArr> imgs = getIntent().getParcelableArrayListExtra("imgs");
                if (imgs.size() > 0) {
                    textView.setText(getIntent().getStringExtra("dis"));
                    for (int i = 0; i < imgs.size(); i++) {
                        adapter.addItem(new SliderItem(Url.http + imgs.get(i).getImageName()));
                    }
                    sliderView.setSliderAdapter(adapter);

                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                    sliderView.setIndicatorSelectedColor(Color.WHITE);
                    sliderView.setIndicatorUnselectedColor(Color.GRAY);
                    sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                    sliderView.startAutoCycle();
                    Log.d("Comment", "onCreate: " + imgs);
                } else {
                    sliderView.setVisibility(View.GONE);
                }
            } catch (Exception e) {

            }
        } else {
            sliderView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
//            materialCardView.setVisibility(View.GONE);

        }
        String url = Url.baseurl + getIntent().getStringExtra("url");//"/ctalk_comments";
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
                        Log.d("ashok_comment", response.toString());
                        ArrayList<CommentData> commentData = new ArrayList<>();
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            Gson gson = new Gson();
                            CommentData comment = gson.fromJson(data.getJSONObject(i).toString(), CommentData.class);
                            commentData.add(comment);
                        }
                        RecyclerView.Adapter commentAdapter = new CommentAdapter(Comment.this, commentData, preferences.getInt(Datas.id, 1), resol);
                        recyclerView.setAdapter(commentAdapter);

//                        JSONArray data_images = response.getJSONArray("data_images");


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
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("token", preferences.getString(Datas.token, ""));
                header.put("lid", preferences.getString(Datas.lagnuage_id, "1"));
                header.put("enc", "");

                return header;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void resolwork() {
        if (!resol) {
            materialCardViewResolved.setVisibility(View.VISIBLE);
        } else {
            materialCardViewResolved.setVisibility(View.GONE);
            comment_box.setVisibility(View.GONE);
            resol = true;
        }
    }

    public void back(View view) {
        finish();
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

    public void newcomment(View view) {
        String s = comment.getText().toString();
        if (isNullOrEmpty(s)) {
            Toast.makeText(this, "Please type comment first", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Url.baseurl + getIntent().getStringExtra("newposturl");
        JSONObject json = new JSONObject();
        try {
            json.put("parentId", getIntent().getStringExtra("pos"));
            json.put("comment", s);
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
                        RecyclerView.Adapter adapter = new CommentAdapter(Comment.this, commentData, preferences.getInt(Datas.id, 1), resol);
                        recyclerView.setAdapter(adapter);
                        comment.setText("");
                    } else
                        Toast.makeText(Comment.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                stopAnim();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comment.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                stopAnim();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("token", preferences.getString(Datas.token, ""));
                header.put("lid", preferences.getString(Datas.lagnuage_id, "1"));
                return header;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void maringresol(View view) {
        String url = Url.baseurl + "/psuggestion_update_status";
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("pid", getIntent().getStringExtra("pos"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ashok", response.toString());
                try {
                    if (response.getBoolean("success")) {
                        Toast.makeText(Comment.this, "success", Toast.LENGTH_SHORT).show();
                        resol = true;
                        resolwork();
                        materialCardViewResolved.setVisibility(View.GONE);
                        ((CommentAdapter) recyclerView.getAdapter()).reol(true);
                        recyclerView.getAdapter().notifyDataSetChanged();
//                        Comment.this.recreate();
                        /*JSONObject data = response.getJSONArray("data").getJSONObject(0);
                        String title = data.getString("title");
                        String description = data.getString("description");

                        textView.setText(description);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            textView.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            textView.setText(Html.fromHtml(description));
                        }
                        JSONArray data_images = response.getJSONArray("data_images");*/
                       /* for (int i = 0; i < data_images.length(); i++) {
                            adapter.addItem(new SliderItem(Url.burl + data_images.getJSONObject(i).getString("image_url")));

                        }
                        sliderView.setSliderAdapter(adapter);

                        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        //sliderView.setAutoCycleDirection(SliderView.AUTO_C);
                        sliderView.setIndicatorSelectedColor(Color.WHITE);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :*/
                        //sliderView.startAutoCycle();
//                        Log.d("ashok", data.toString());

                    } else {
                        Toast.makeText(Comment.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Comment.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(Comment.this, LoginActivity.class));
                    }
                    Log.d("ashok", response.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                stopAnim();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comment.this, "" + error, Toast.LENGTH_SHORT).show();
                Log.d("ashok", error.toString());
                stopAnim();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("token", preferences.getString(Datas.token, ""));
                headers.put("lid", preferences.getString(Datas.lagnuage_id, "1"));
                Log.d("ashok", preferences.getString(Datas.lagnuage_id, "1"));
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}