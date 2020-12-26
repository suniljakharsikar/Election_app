package b2d.l.mahtmagandhi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    private EditText editText;
    boolean issubmit = false;
    //    ImageView imageView;
    private AVLoadingIndicatorView avi;
    private String mobile;
    private int getcode = 1111;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private String TAG = "ashok";
    CountryCodePicker countryCodePicker;
    static PhoneAuthProvider.ForceResendingToken token;

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
        mAuth = FirebaseAuth.getInstance();
        countryCodePicker = findViewById(R.id.ccp);
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
        mobile = editText.getText().toString();
        if (mobile.length() != 10) {
            Toast.makeText(this, "Please enter correct 10 digit mobile number", Toast.LENGTH_SHORT).show();
            return;
        }
//        sendotp(mobile);//api
        sendotpusingfirebase(mobile);//firebase otp
    }

    private void sendotpusingfirebase(String phoneNumber) {
        startAnim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Toast.makeText(LoginActivity.this, "verified", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                stopAnim();
                Toast.makeText(LoginActivity.this, "failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                editText.setFocusable(true);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
//                stopAnim();
                mVerificationId = s;
                Toast.makeText(LoginActivity.this, "code sent", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, OTPVerify.class);
                intent.putExtra("mobile", countryCodePicker.getSelectedCountryCodeWithPlus() + mobile);
                token = forceResendingToken;
                intent.putExtra("forceResendingToken", forceResendingToken);
//                intent.putExtra("mCallbacks", mCallbacks);
                startActivityForResult(intent, getcode);
//                startActivity(intent);
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(countryCodePicker.getSelectedCountryCodeWithPlus() + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
//        Toast.makeText(this, "" + countryCodePicker.getSelectedCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.verifyPhoneNumber(options);
        editText.setFocusable(false);

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
//                            updateUI(STATE_SIGNIN_SUCCESS, user);
//                            Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();

                            String url = Url.baseurl + "/users";
                            JSONObject jsonrequest = new JSONObject();
                            try {
                                jsonrequest.put("userMobile", countryCodePicker.getSelectedCountryCodeWithPlus() + mobile);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonrequest, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        boolean success = response.getBoolean("success");
                                        if (success) {
                                            String token = response.getString("token");
                                            JSONArray data1 = response.getJSONArray("data");
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putBoolean(Datas.loginstatus, true);
                                            JSONObject jsonObject = data1.getJSONObject(0);
                                            editor.putInt(Datas.id, jsonObject.getInt(Datas.id));
                                            if (!jsonObject.getString(Datas.user_name).equals("null")) {
                                                editor.putString(Datas.user_name, jsonObject.getString(Datas.user_name));
                                            }
                                            editor.putString(Datas.user_mobile, jsonObject.getString(Datas.user_mobile));
                                            if (!jsonObject.getString(Datas.user_age).equals("null"))
                                                editor.putString(Datas.user_age, jsonObject.getString(Datas.user_age));
                                            if (!jsonObject.getString(Datas.user_postal_code).equals("null"))
                                                editor.putString(Datas.user_postal_code, jsonObject.getString(Datas.user_postal_code));
                                            if (!jsonObject.getString(Datas.user_state).equals("null"))
                                                editor.putString(Datas.user_state, jsonObject.getString(Datas.user_state));
                                            if (!jsonObject.getString(Datas.user_district).equals("null"))
                                                editor.putString(Datas.user_district, jsonObject.getString(Datas.user_district));
                                            if (!jsonObject.getString(Datas.user_village).equals("null"))
                                                editor.putString(Datas.user_village, jsonObject.getString(Datas.user_village));
                                            if (!jsonObject.getString(Datas.lagnuage_id).equals("null"))
                                                editor.putString(Datas.lagnuage_id, jsonObject.getString(Datas.lagnuage_id));
                                            editor.putString(Datas.token, token);
                                            editor.apply();
                                            startActivity(new Intent(LoginActivity.this, Register.class));
                                            Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                            finish();

                                        } else {
                                            Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                            //login page
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.clear();
                                            editor.apply();
                                            startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
//                                        sendbtn.setEnabled(true);

                                    }

                                    stopAnim();


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Log.d("ashok", error.toString());
                                }
                            });
                            MySingleton.getInstance(LoginActivity.this).addToRequestQueue(jsonObjectRequest);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
//                                mBinding.fieldVerificationCode.setError("Invalid code.");
                                Toast.makeText(LoginActivity.this, "Invalid code", Toast.LENGTH_SHORT).show();
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
//                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    // [END sign_in_with_phone]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == getcode) {
            if (data != null) {
                String code = data.getStringExtra("code");
                verifyPhoneNumberWithCode(mVerificationId, code);

            }
        }
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