package b2d.l.mahtmagandhi;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import kotlinx.coroutines.GlobalScope;

public class OTPVerify extends AppCompatActivity {

    private EditText editText1, editText2, editText3, editText4, editText5, editText6;
    private EditText[] editTexts;
    Button sendbtn;
    boolean isOtpSent = false;
    private AVLoadingIndicatorView avi;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String TAG = "Ashok";
    private FirebaseAuth mAuth;

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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        avi = findViewById(R.id.avi);

        ConstraintLayout constraintLayout  = findViewById(R.id.cl_otp);
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(constraintLayout
                .getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        editText1 = (EditText) findViewById(R.id.otpEdit1);
        editText2 = (EditText) findViewById(R.id.otpEdit2);
        editText3 = (EditText) findViewById(R.id.otpEdit3);
        editText4 = (EditText) findViewById(R.id.otpEdit4);
        editText5 = (EditText) findViewById(R.id.otpEdit5);
        editText6 = (EditText) findViewById(R.id.otpEdit6);
        editTexts = new EditText[]{editText1, editText2, editText3, editText4, editText5, editText6};
        sendbtn = findViewById(R.id.button_verify_otp);

        editText1.addTextChangedListener(new PinTextWatcher(0));
        editText2.addTextChangedListener(new PinTextWatcher(1));
        editText3.addTextChangedListener(new PinTextWatcher(2));
        editText4.addTextChangedListener(new PinTextWatcher(3));
        editText5.addTextChangedListener(new PinTextWatcher(4));
        editText6.addTextChangedListener(new PinTextWatcher(5));

        editText1.setOnKeyListener(new PinOnKeyListener(0));
        editText2.setOnKeyListener(new PinOnKeyListener(1));
        editText3.setOnKeyListener(new PinOnKeyListener(2));
        editText4.setOnKeyListener(new PinOnKeyListener(3));
        editText5.setOnKeyListener(new PinOnKeyListener(4));
        editText6.setOnKeyListener(new PinOnKeyListener(5));
        stopAnim();
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Toast.makeText(OTPVerify.this, "verified", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                stopAnim();
                Toast.makeText(OTPVerify.this, "failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                editText.setFocusable(true);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                stopAnim();
//                mVerificationId = s;
                Toast.makeText(OTPVerify.this, "PTO sent successfully", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(OTPVerify.this, OTPVerify.class);
//                intent.putExtra("mobile", mobile);
//                intent.putExtra("forceResendingToken", forceResendingToken);
//                intent.putExtra("mCallbacks", mCallbacks);
//                startActivityForResult(intent, getcode);
//                startActivity(intent);
            }
        };
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
                                jsonrequest.put("userMobile", getIntent().getStringExtra("mobile"));
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
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OTPVerify.this);
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
                                            stopAnim();
                                            Toast.makeText(OTPVerify.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(OTPVerify.this, Register.class));

                                            finish();

                                        } else {
                                            Toast.makeText(OTPVerify.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                            //login page
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OTPVerify.this);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.clear();
                                            editor.apply();
                                            startActivity(new Intent(OTPVerify.this, LoginActivity.class));
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
                            MySingleton.getInstance(OTPVerify.this).addToRequestQueue(jsonObjectRequest);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
//                                mBinding.fieldVerificationCode.setError("Invalid code.");
                                Toast.makeText(OTPVerify.this, "Invalid code", Toast.LENGTH_SHORT).show();
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

    public void sumit(View view) {

        String otp =
                editText1.getText().toString() +
                        editText2.getText().toString() +
                        editText3.getText().toString() +
                        editText4.getText().toString() +
                        editText5.getText().toString() +
                        editText6.getText().toString();
        if (otp.length() == 6) {
//            verifyotp(otp);
            verifyfirebse(otp);
        } else {
            Toast.makeText(this, "please type 6 digit otp", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyfirebse(String otp) {
        Intent intent = getIntent();
        intent.putExtra("code", otp);
        setResult(1111, intent);
        finish();
    }

    private void verifyotp(String otp) {
        /*if (!isOtpSent) {
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "in progress please wait", Toast.LENGTH_SHORT).show();

        }
        isOtpSent = true;*/
        String url = Url.baseurl + "/otp_verify";
        JSONObject data = new JSONObject();
        try {
            data.put("userMobile", getIntent().getStringExtra("mobile"));
            data.put("otp", otp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startAnim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        String token = response.getString("token");
                        JSONArray data1 = response.getJSONArray("data");
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OTPVerify.this);
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
                        if (!jsonObject.getString(Datas.user_age).equals("null"))
                            editor.putString(Datas.user_postal_code, jsonObject.getString(Datas.user_postal_code));
                        if (!jsonObject.getString(Datas.user_age).equals("null"))
                            editor.putString(Datas.user_state, jsonObject.getString(Datas.user_state));
                        if (!jsonObject.getString(Datas.user_age).equals("null"))
                            editor.putString(Datas.user_district, jsonObject.getString(Datas.user_district));
                        if (!jsonObject.getString(Datas.user_age).equals("null"))
                            editor.putString(Datas.lagnuage_id, jsonObject.getString(Datas.lagnuage_id));
                        editor.putString(Datas.token, token);
                        editor.apply();
                        startActivity(new Intent(OTPVerify.this, Register.class));
                        Toast.makeText(OTPVerify.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(OTPVerify.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OTPVerify.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(OTPVerify.this, LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendbtn.setEnabled(true);

                }

                stopAnim();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendbtn.setEnabled(true);
                stopAnim();
                Toast.makeText(OTPVerify.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
//        requestQueue.add(jsonObjectRequest);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        sendbtn.setEnabled(false);
    }

    public void resend(View view) {
        startAnim();
        resendVerificationCode(getIntent().getStringExtra("mobile"), LoginActivity.token);
    }

    /*private void resendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                (PhoneAuthProvider.ForceResendingToken) getIntent().getParcelableExtra("forceResendingToken"));             // ForceResendingToken from callbacks
    }*/
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

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

    public void back(View view) {
        Intent intent = getIntent();
        setResult(1112, intent);
        finish();
    }
}