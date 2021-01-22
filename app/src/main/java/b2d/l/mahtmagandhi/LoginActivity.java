package b2d.l.mahtmagandhi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
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
    private String tokenid;
    ConstraintLayout mobile_c, otp_c;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private EditText[] editTexts;

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
    protected void onResume() {
        super.onResume();
        /*if (getIntent().hasExtra("from")) {
            Toast.makeText(this, "has extra", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NotificationActivity.class));
//            finish()l

        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loginstatus = preferences.getBoolean("loginstatus", false);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        tokenid = task.getResult();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        if (loginstatus) {
            startActivity(new Intent(LoginActivity.this, Register.class));
            finish();
        }
    }

    private EditText editText1, editText2, editText3, editText4, editText5, editText6;

    public void back(View view) {
        finish();
    }

    public void submit(View view) {
//        Toast.makeText(this, "testing", Toast.LENGTH_SHORT).show();
        mobile = editText.getText().toString().trim();

        if (mobile.length()< 6 ) {

            editText.setError("Please Input your valid Mobile no.");

        } else {

        /*if (mobile.length() != 10) {
            Toast.makeText(this, "Please enter correct 10 digit mobile number", Toast.LENGTH_SHORT).show();
            return;
        }*/
            if (Url.firebaseOTP) {
                sendotpusingfirebase(mobile);//firebase otp
            } else {
                sendotp(mobile);//api

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mobile_c = findViewById(R.id.mobile);
        otp_c = findViewById(R.id.otp);
        avi = findViewById(R.id.avi);
        mAuth = FirebaseAuth.getInstance();
        countryCodePicker = findViewById(R.id.ccp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        editText = findViewById(R.id.editTextPhone3);

        editText1 = (EditText) findViewById(R.id.otpEdit1);
        editText2 = (EditText) findViewById(R.id.otpEdit2);
        editText3 = (EditText) findViewById(R.id.otpEdit3);
        editText4 = (EditText) findViewById(R.id.otpEdit4);
        editText5 = (EditText) findViewById(R.id.otpEdit5);
        editText6 = (EditText) findViewById(R.id.otpEdit6);
        editTexts = new EditText[]{editText1, editText2, editText3, editText4, editText5, editText6};
//        sendbtn = findViewById(R.id.button_verify_otp);

        editText1.addTextChangedListener(new LoginActivity.PinTextWatcher(0));
        editText2.addTextChangedListener(new LoginActivity.PinTextWatcher(1));
        editText3.addTextChangedListener(new LoginActivity.PinTextWatcher(2));
        editText4.addTextChangedListener(new LoginActivity.PinTextWatcher(3));
        editText5.addTextChangedListener(new LoginActivity.PinTextWatcher(4));
        editText6.addTextChangedListener(new LoginActivity.PinTextWatcher(5));

        editText1.setOnKeyListener(new LoginActivity.PinOnKeyListener(0));
        editText2.setOnKeyListener(new LoginActivity.PinOnKeyListener(1));
        editText3.setOnKeyListener(new LoginActivity.PinOnKeyListener(2));
        editText4.setOnKeyListener(new LoginActivity.PinOnKeyListener(3));
        editText5.setOnKeyListener(new LoginActivity.PinOnKeyListener(4));
        editText6.setOnKeyListener(new LoginActivity.PinOnKeyListener(5));
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

    private void sendotpusingfirebase(String phoneNumber) {
        startAnim();
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Toast.makeText(LoginActivity.this, "verified", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(LoginActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(LoginActivity.this, "The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show();
                    // ...
                } else {
                    Toast.makeText(LoginActivity.this, "failed =" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

                // Show a message and update the UI
                // ...
                stopAnim();
                editText.setFocusable(true);
                mobile_c.setVisibility(View.VISIBLE);
                otp_c.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);

                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
                Toast.makeText(LoginActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();

                stopAnim();
//                mVerificationId = s;
                mobile_c.setVisibility(View.GONE);
                otp_c.setVisibility(View.VISIBLE);
               /* Intent intent = new Intent(LoginActivity.this, OTPVerify.class);
                intent.putExtra("mobile", countryCodePicker.getSelectedCountryCodeWithPlus() + mobile);
                intent.putExtra("token", tokenid);
                token = forceResendingToken;
                intent.putExtra("forceResendingToken", forceResendingToken);
                intent.putExtra("mVerificationId", s);
                stopAnim();
//                startActivityForResult(intent, getcode);
                startActivity(intent);*/

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
                                if (tokenid == null) {
                                    FirebaseMessaging.getInstance().getToken()
                                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                                @Override
                                                public void onComplete(@NonNull Task<String> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                        return;
                                                    }

                                                    // Get new FCM registration token
                                                    tokenid = task.getResult();
                                                    Log.d("tokenid", tokenid);


                                                    // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                jsonrequest.put("fcm_token", tokenid);
//                                Toast.makeText(LoginActivity.this, "token " + tokenid, Toast.LENGTH_SHORT).show();
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
                startAnim();
                String code = data.getStringExtra("code");
                verifyPhoneNumberWithCode(mVerificationId, code);

            }
        } else if (requestCode == 1112) {
            stopAnim();
//            Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(LoginActivity.this, OTPVerifyModify.class);
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

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        startAnim();
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    public void resend(View view) {
    }

    public void sumit(View view) {
        String code = editText1.getText().toString() +
                editText2.getText().toString() +
                editText3.getText().toString() +
                editText4.getText().toString() +
                editText5.getText().toString() +
                editText6.getText().toString();
        ;
        verifyPhoneNumberWithCode(mVerificationId, code);
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
    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1) {

                this.isLast = true;
                hideKeyboard();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();

            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }
}