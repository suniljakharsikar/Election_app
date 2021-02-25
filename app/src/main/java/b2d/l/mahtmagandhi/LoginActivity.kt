package b2d.l.mahtmagandhi

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import b2d.l.mahtmagandhi.LoginActivity
import b2d.l.mahtmagandhi.OTPVerifyModify
import b2d.l.mahtmagandhi.Register
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.messaging.FirebaseMessaging
import com.hbb20.CountryCodePicker
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_o_t_p_modify.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.math.BigInteger
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    var circleImageView: CircleImageView? = null
    private var editText: EditText? = null
    var issubmit = false

    //    ImageView imageView;
    private var avi: ProgressBar? = null
    private var mobile: String? = null
    private val getcode = 1111
    private var mVerificationId: String? = null
    private var mAuth: FirebaseAuth? = null
    private val TAG = "ashok"
    var countryCodePicker: CountryCodePicker? = null
    private var tokenid: String? = null
    var mobile_c: ConstraintLayout? = null
    var otp_c: ConstraintLayout? = null
    private var mResendToken: ForceResendingToken? = null
    fun startAnim() {
        avi!!.visibility = View.VISIBLE
        // or avi.smoothToShow();
    }

    fun stopAnim() {
        avi!!.visibility = View.INVISIBLE
        //        avi.hide();
        // or avi.smoothToHide();
    }

    override fun onResume() {
        super.onResume()
        /*if (getIntent().hasExtra("from")) {
            Toast.makeText(this, "has extra", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NotificationActivity.class));
//            finish()l

        }*/
    }

    override fun onStart() {
        super.onStart()
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val loginstatus = preferences.getBoolean("loginstatus", false)
        FirebaseMessaging.getInstance().token
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    tokenid = task.result

                    // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                })
        if (loginstatus) {
            // FIXME: 08-02-2021 Home
            startActivity(Intent(this@LoginActivity, Register::class.java))
            finish()
        }
    }

    private var editText1: EditText? = null

    fun back(view: View?) {
        finish()
    }

    fun submit(view: View?) {
//        Toast.makeText(this, "testing", Toast.LENGTH_SHORT).show();
        Utility.hideKeyboard(this)
        mobile = editText!!.text.toString().trim { it <= ' ' }
        if (mobile!!.length < 6 ) {
            editText!!.error = "Enter Valid Mobile Number"
        }else if(mobile!!.toBigIntegerOrNull()== BigInteger.ZERO){
            editText!!.error = "Enter Valid Mobile Number."

        }else if (mobile!!.length >15 ) {
            editText!!.error = "Enter Valid Mobile Number" }
        else {

            val job = GlobalScope.async {
                return@async Utility.isInternetAvailable()
            }
            job.invokeOnCompletion {
                val isInternet = job.getCompleted()
                GlobalScope.launch(Dispatchers.Main) {
                    if (isInternet) {

                        if (Url.firebaseOTP) {
                            sendotpusingfirebase(mobile!!) //firebase otp
                        } else {
                            if(countryCodePicker!!.selectedCountryCodeWithPlus.equals("+91"))
                            sendotp(mobile!!) //api
                            else
                                customSnackBar(editText!!, this@LoginActivity, "This app working in India only.", ContextCompat.getColor(this@LoginActivity, R.color.error), R.drawable.ic_error)


                        }


                    } else {
                        stopAnim()
                        customSnackBar(editText!!, this@LoginActivity, "No Internet Available.", ContextCompat.getColor(this@LoginActivity, R.color.error), R.drawable.ic_error)
                    }
                }

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mobile_c = findViewById(R.id.mobile)
        otp_c = findViewById(R.id.otp)
        avi = findViewById(R.id.avi)
        mAuth = FirebaseAuth.getInstance()
        countryCodePicker = findViewById(R.id.ccp)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        editText = findViewById(R.id.editTextPhone3)
        editText1 = findViewById<View>(R.id.otpEdit1) as EditText


        stopAnim()
    }

    private fun sendotpusingfirebase(phoneNumber: String) {
        startAnim()
        val mCallbacks: OnVerificationStateChangedCallbacks = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Toast.makeText(LoginActivity.this, "verified", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this@LoginActivity, "Invalid request", Toast.LENGTH_SHORT).show()
                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(this@LoginActivity, "The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show()
                    // ...
                } else {
                    Toast.makeText(this@LoginActivity, "failed =" + e.message, Toast.LENGTH_SHORT).show()
                }

                // Show a message and update the UI
                // ...
                stopAnim()
                editText!!.isFocusable = true
                mobile_c!!.visibility = View.VISIBLE
                otp_c!!.visibility = View.GONE
            }

            override fun onCodeSent(verificationId: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken)

                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token
                timerForResend()
                // ...
                if (Datas.DEBUG) Toast.makeText(this@LoginActivity, "OTP sent successfully", Toast.LENGTH_SHORT).show()
                stopAnim()
                //                mVerificationId = s;
                mobile_c!!.visibility = View.GONE
                otp_c!!.visibility = View.VISIBLE
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
        }
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
                .setPhoneNumber(countryCodePicker!!.selectedCountryCodeWithPlus + phoneNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
                .build()
        //        Toast.makeText(this, "" + countryCodePicker.getSelectedCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.verifyPhoneNumber(options)
        editText!!.isFocusable = false
    }

    private var timer: CountDownTimer? = null


    private fun timerForResend() {
        if (timer!=null)
            timer!!.cancel()

        textView_resend_otp_modify.isEnabled = false
        textView_resend_otp_modify.setTextColor(ContextCompat.getColor(this, R.color.browser_actions_divider_color))

        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                textView_sec_otp_modify.setText("Resend in " + millisUntilFinished / 1000 + "sec")
                textView_sec_otp_modify.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.black))

                if (textView_resend_otp_modify.visibility == View.VISIBLE)
                    textView_resend_otp_modify.visibility = View.GONE
            }

            override fun onFinish() {
                textView_resend_otp_modify.isEnabled = true
                textView_resend_otp_modify.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.black))
                textView_sec_otp_modify.setText("")
                textView_resend_otp_modify.visibility = View.VISIBLE
            }
        }.start()


    }
    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = task.result.user
                        // [START_EXCLUDE]
//                            updateUI(STATE_SIGNIN_SUCCESS, user);
//                            Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                        val url = Url.baseurl + "/users"
                        val jsonrequest = JSONObject()
                        try {
                            jsonrequest.put("userMobile", countryCodePicker!!.selectedCountryCodeWithPlus + mobile)
                            if (tokenid == null) {
                                FirebaseMessaging.getInstance().token
                                        .addOnCompleteListener(OnCompleteListener { task ->
                                            if (!task.isSuccessful) {
                                                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                                return@OnCompleteListener
                                            }

                                            // Get new FCM registration token
                                            tokenid = task.result
                                            Log.d("tokenid", tokenid)


                                            // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        })
                            }
                            jsonrequest.put("fcm_token", tokenid)
                            //                                Toast.makeText(LoginActivity.this, "token " + tokenid, Toast.LENGTH_SHORT).show();
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonrequest, { response ->
                            try {
                                val success = response.getBoolean("success")
                                if (success) {
                                    val token = response.getString("token")
                                    val data1 = response.getJSONArray("data")
                                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                                    val editor = preferences.edit()
                                    editor.putBoolean(Datas.loginstatus, true)
                                    val jsonObject = data1.getJSONObject(0)
                                    editor.putInt(Datas.id, jsonObject.getInt(Datas.id))
                                    if (jsonObject.getString(Datas.user_name) != "null") {
                                        editor.putString(Datas.user_name, jsonObject.getString(Datas.user_name))
                                    }
                                    editor.putString(Datas.user_mobile, jsonObject.getString(Datas.user_mobile))
                                    if (jsonObject.getString(Datas.user_age) != "null") editor.putString(Datas.user_age, jsonObject.getString(Datas.user_age))
                                    if (jsonObject.getString(Datas.user_postal_code) != "null") editor.putString(Datas.user_postal_code, jsonObject.getString(Datas.user_postal_code))
                                    if (jsonObject.getString(Datas.user_state) != "null") editor.putString(Datas.user_state, jsonObject.getString(Datas.user_state))
                                    if (jsonObject.getString(Datas.user_district) != "null") editor.putString(Datas.user_district, jsonObject.getString(Datas.user_district))
                                    if (jsonObject.getString(Datas.user_village) != "null") editor.putString(Datas.user_village, jsonObject.getString(Datas.user_village))
                                    if (jsonObject.getString(Datas.lagnuage_id) != "null") editor.putString(Datas.lagnuage_id, jsonObject.getString(Datas.lagnuage_id))
                                    editor.putString(Datas.token, token)
                                    editor.apply()
                                    startActivity(Intent(this@LoginActivity, Register::class.java))
                                    // Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                    finish()
                                } else {
                                    // Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                    //login page
                                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                                    val editor = preferences.edit()
                                    editor.clear()
                                    editor.apply()
                                    startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                //                                        sendbtn.setEnabled(true);
                            }
                            stopAnim()
                        }) { error -> Log.d("ashok", error.toString()) }
                        MySingleton.getInstance(this@LoginActivity).addToRequestQueue(jsonObjectRequest)
                        // [END_EXCLUDE]
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            // [START_EXCLUDE silent]
//                                mBinding.fieldVerificationCode.setError("Invalid code.");
                            Toast.makeText(this@LoginActivity, "Invalid code", Toast.LENGTH_SHORT).show()
                            // [END_EXCLUDE]
                        }
                        // [START_EXCLUDE silent]
                        // Update UI
//                            updateUI(STATE_SIGNIN_FAILED);
                        // [END_EXCLUDE]
                    }
                })
    }

    // [END sign_in_with_phone]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == getcode) {
            if (data != null) {
                startAnim()
                val code = data.getStringExtra("code")
                verifyPhoneNumberWithCode(mVerificationId, code)
            }
        } else if (requestCode == 1112) {
            stopAnim()
            //            Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
        }
    }

    private fun sendotp(mobile: String) {
        if (issubmit) {
            Toast.makeText(this, "already in progress", Toast.LENGTH_SHORT).show()
        } else {
            issubmit = true
            // Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();
            val url = Url.baseurl + "/user_onboard"
            val json = JSONObject()
            try {
                json.put("userMobile", mobile)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            startAnim()
            val requestQueue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, json, { response ->
                try {
                    if (response.getBoolean("success")) {
                        val intent = Intent(this@LoginActivity, OTPVerifyModify::class.java)
                        intent.putExtra("mobile", mobile)
                        // Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(intent)
                        finish()
                    } else {
                        issubmit = false
                        // Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        customSnackBar(mobile_c!!, this@LoginActivity, response.getString("message").toString(),
                                ContextCompat.getColor(this@LoginActivity, R.color.error), R.drawable.ic_error) /*//login page
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, LoginActivity.class));*/
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                stopAnim()
            }) { error ->
                issubmit = false
                stopAnim()
                customSnackBar(mobile_c!!, this@LoginActivity, error.toString(),
                        ContextCompat.getColor(this@LoginActivity, R.color.error), R.drawable.ic_error)
                //Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
            //            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
            val socketTimeout = 10000 //10 seconds - change to what you want
            val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            jsonObjectRequest.retryPolicy = policy
            requestQueue.add(jsonObjectRequest)
        }
    }

    fun clear(view: View?) {
        editText!!.setText("")
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        startAnim()
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    fun resend(view: View?) {
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {
            val isInternet = job.getCompleted()
            GlobalScope.launch(Dispatchers.Main) {
                if (isInternet) {

                    if (Url.firebaseOTP) {
                        sendotpusingfirebase(mobile!!) //firebase otp
                    } else {
                        if(countryCodePicker!!.selectedCountryCodeWithPlus.equals("+91"))
                            sendotp(mobile!!) //api
                        else
                            customSnackBar(editText!!, this@LoginActivity, "This app working in India only.", ContextCompat.getColor(this@LoginActivity, R.color.error), R.drawable.ic_error)

                    }


                } else {
                    stopAnim()
                    customSnackBar(editText!!, this@LoginActivity, "No Internet Available.", ContextCompat.getColor(this@LoginActivity, R.color.error), R.drawable.ic_error)
                }
            }

        }
    }
    fun sumit(view: View?) {
        val code = editText1!!.text.toString()
        verifyPhoneNumberWithCode(mVerificationId, code)
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
    inner class PinTextWatcher internal constructor(private val currentIndex: Int) : TextWatcher {
        private var isFirst = false
        private var isLast = false
        private var newTypedString = ""
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            newTypedString = s.subSequence(start, start + count).toString().trim { it <= ' ' }
        }

        override fun afterTextChanged(s: Editable) {
            var text = newTypedString

            /* Detect paste event and set first char */if (text.length > 1) text = text[0].toString() // TODO: We can fill out other EditTexts

        }




        private fun hideKeyboard() {
            if (currentFocus != null) {
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
        }


    }



    companion object {
        @JvmField
        var token: ForceResendingToken? = null
    }
}