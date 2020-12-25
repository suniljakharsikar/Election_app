package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallClipRotatePulseIndicator;
import com.wang.avi.indicators.BallPulseIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AboutUs extends AppCompatActivity {

    private SharedPreferences preferences;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        avi = findViewById(R.id.avi);
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        final SliderView sliderView = findViewById(R.id.imageSlider);

        final SliderAdapterExample adapter = new SliderAdapterExample(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        /*adapter.addItem(new SliderItem());
        adapter.addItem(new SliderItem());
        adapter.addItem(new SliderItem());
        sliderView.setSliderAdapter(adapter);*/

        textView = findViewById(R.id.textView16);


        String url = Url.baseurl + "/about_us";
        JSONObject jsonRequest = new JSONObject();
        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ashok", response.toString());
                try {
                    if (response.getBoolean("success")) {
                        JSONObject data = response.getJSONArray("data").getJSONObject(0);
                        String title = data.getString("title");
                        String description = data.getString("description");

                        textView.setText(description);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            textView.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            textView.setText(Html.fromHtml(description));
                        }
                        JSONArray data_images = response.getJSONArray("data_images");
                        for (int i = 0; i < data_images.length(); i++) {
                            adapter.addItem(new SliderItem(Url.burl+data_images.getJSONObject(i).getString("image_url")));

                        }
                        sliderView.setSliderAdapter(adapter);

                        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        sliderView.setIndicatorSelectedColor(Color.WHITE);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                        sliderView.startAutoCycle();
//                        Log.d("ashok", data.toString());

                    } else {
                        Toast.makeText(AboutUs.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AboutUs.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(AboutUs.this, LoginActivity.class));
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
                Toast.makeText(AboutUs.this, "" + error, Toast.LENGTH_SHORT).show();
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

    public void back(View view) {
        finish();
    }
}