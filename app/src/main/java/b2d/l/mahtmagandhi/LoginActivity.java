package b2d.l.mahtmagandhi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    private EditText editText;
    boolean issubmit = false;
//    ImageView imageView;
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
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loginstatus = preferences.getBoolean("loginstatus", false);
        if (loginstatus) {
            startActivity(new Intent(LoginActivity.this, Register.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        avi = findViewById(R.id.avi);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        editText = findViewById(R.id.editTextPhone3);
//        imageView = findViewById(R.id.subbtn);
       /* Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);*/
//        int statusBarHeight = rectangle.top;
//        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
//        int titleBarHeight = contentViewTop - statusBarHeight;
//        Log.d("ashok", "s=" + statusBarHeight + " t= " + titleBarHeight);
//        Toast.makeText(this, "s=" + statusBarHeight + " t= " + titleBarHeight, Toast.LENGTH_SHORT).show();

       /* TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -200);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());

        circleImageView = findViewById(R.id.profile_image);
        circleImageView.startAnimation(animation);*/

       /* String url = "http://election.suniljakhar.in/api/user_onboard";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(LoginActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
//                        textView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);*/

        stopAnim();

    }

    public void back(View view) {
        finish();
    }

    public void submit(View view) {
        String mobile = editText.getText().toString();
        if (mobile.length() != 10) {
            Toast.makeText(this, "Please enter correct 10 digit mobile number", Toast.LENGTH_SHORT).show();
            return;
        }
        sendotp(mobile);
    }

    private void sendotp(final String mobile) {
        if (issubmit) {
            Toast.makeText(this, "already in progress", Toast.LENGTH_SHORT).show();
        } else {
            issubmit = true;
            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();
            String url = Url.baseurl + "/user_onboard";
            JSONObject json = new JSONObject();
            try {
                json.put("userMobile", mobile);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startAnim();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("success")) {
                            Intent intent = new Intent(LoginActivity.this, OTPVerify.class);
                            intent.putExtra("mobile", mobile);
                            Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        } else {
                            issubmit = false;
                            Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                            /*//login page
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, LoginActivity.class));*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    stopAnim();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    issubmit = false;
                    stopAnim();
                    Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                }
            });
//            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);

        }
    }

    public void clear(View view) {
        editText.setText("");
    }

    /*private void sendotp(final String mobile) {
        if (issubmit) {
            Toast.makeText(this, "already clicked ", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            issubmit = true;
            imageView.setEnabled(false);
        }
        try {
            String URL = Url.baseurl + "/user_onboard";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("userMobile", mobile);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, OTP.class);
                            intent.putExtra("mobile", mobile);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

//            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}