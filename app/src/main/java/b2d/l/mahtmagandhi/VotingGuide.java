package b2d.l.mahtmagandhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VotingGuide extends YouTubeBaseActivity {
    TextView textView;
    private AVLoadingIndicatorView avi;
    private YouTubePlayerView youTubePlayerView;

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
        setContentView(R.layout.activity_voting_guide);
        avi = findViewById(R.id.avi);
        youTubePlayerView = findViewById(R.id.youtube_voting_guide);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        textView = findViewById(R.id.tv);
        String url = Url.baseurl + "/guide";
        JSONObject json = new JSONObject();

        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ashok_voting", response.toString());
                try {
                    if (response.getBoolean("success")) {
                        String description = response.getJSONArray("data").getJSONObject(0).getString("description");
                        String media = response.getJSONArray("data").getJSONObject(0).getString("media");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            textView.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            textView.setText(Html.fromHtml(description));
                        }
                        youTubePlayerView.initialize("AIzaSyD5IrFsnu-cC6ODQM1r_KcIg_G584aBvzY", new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                                try {
                                    String[] ms = media.split("=");
                                    if (ms.length>0) {
                                        youTubePlayer.loadVideo(ms[1]);

                                        youTubePlayer.play();
                                    }
                                } catch (Exception e) {

                                }

                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                            }
                        });

                        //textView.setText(description);
//                        Toast.makeText(VotingGuide.this, "" + response.toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(VotingGuide.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(VotingGuide.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(VotingGuide.this, LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(VotingGuide.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                }
                stopAnim();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopAnim();
                Utility.INSTANCE.customSnackBar(textView,VotingGuide.this,error.toString(),
                        ContextCompat.getColor(VotingGuide.this,R.color.error),R.drawable.ic_error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(VotingGuide.this);
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