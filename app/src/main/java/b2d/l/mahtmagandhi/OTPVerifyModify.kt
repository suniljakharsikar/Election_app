package b2d.l.mahtmagandhi

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.messaging.FirebaseMessaging
import com.wang.avi.AVLoadingIndicatorView
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class OTPVerifyModify : AppCompatActivity() {
    private var editText1: EditText? = null
    var sendbtn: Button? = null
    var isOtpSent = false
    private var avi: AVLoadingIndicatorView? = null
    private var mCallbacks: OnVerificationStateChangedCallbacks? = null
    private val TAG = "Ashok"
    private var mAuth: FirebaseAuth? = null
    private var verificationId: String? = null
    private var mobile: String? = null
    private var tokenid: String? = null
    private var called = false
    fun startAnim() {
        avi!!.show()
        avi!!.visibility = View.VISIBLE
        // or avi.smoothToShow();
    }

    fun stopAnim() {
        avi!!.visibility = View.INVISIBLE
        //        avi.hide();
        // or avi.smoothToHide();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_t_p_modify)
        tokenid = intent.getStringExtra("token")
        avi = findViewById(R.id.avi)
        verificationId = intent.getStringExtra("mVerificationId")
        mobile = intent.getStringExtra("mobile")
        val constraintLayout = findViewById<ConstraintLayout>(R.id.cl_otp)
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(constraintLayout
                .applicationWindowToken,
                InputMethodManager.SHOW_FORCED, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        editText1 = findViewById<View>(R.id.otpEdit1) as EditText


        sendbtn = findViewById(R.id.button_verify_otp)
        stopAnim()
        mAuth = FirebaseAuth.getInstance()
        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                serverlogin()
            }
        }
        mCallbacks = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                Toast.makeText(OTPVerify.this, "verified", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                stopAnim()
                Toast.makeText(this@OTPVerifyModify, "failed " + e.message, Toast.LENGTH_SHORT).show()
                //                editText.setFocusable(true);
            }

            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                stopAnim()
                //                mVerificationId = s;
                Toast.makeText(this@OTPVerifyModify, "OTP sent successfully", Toast.LENGTH_SHORT).show()
                //                Intent intent = new Intent(OTPVerify.this, OTPVerify.class);
//                intent.putExtra("mobile", mobile);
//                intent.putExtra("forceResendingToken", forceResendingToken);
//                intent.putExtra("mCallbacks", mCallbacks);
//                startActivityForResult(intent, getcode);
//                startActivity(intent);
            }
        }
    }

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = task.result.user
                        // [START_EXCLUDE]
//                            updateUI(STATE_SIGNIN_SUCCESS, user);
//                            Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                        serverlogin()
                        // [END_EXCLUDE]
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            // [START_EXCLUDE silent]
//                                mBinding.fieldVerificationCode.setError("Invalid code.");
                            Toast.makeText(this@OTPVerifyModify, "Invalid code", Toast.LENGTH_SHORT).show()
                            // [END_EXCLUDE]
                        }
                        // [START_EXCLUDE silent]
                        // Update UI
//                            updateUI(STATE_SIGNIN_FAILED);
                        // [END_EXCLUDE]
                    }
                }
    }

    private fun serverlogin() {
        if (called) return
        called = true
        Toast.makeText(this@OTPVerifyModify, "OTP verified", Toast.LENGTH_SHORT).show()
        val url = Url.baseurl + "/users"
        val jsonrequest = JSONObject()
        try {
            jsonrequest.put("userMobile", mobile)
            jsonrequest.put("fcm_token", tokenid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonrequest, { response ->
            try {
                val success = response.getBoolean("success")
                if (success) {
                    val token = response.getString("token")
                    val data1 = response.getJSONArray("data")
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@OTPVerifyModify)
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
                    startActivity(Intent(this@OTPVerifyModify, Register::class.java))
                    Toast.makeText(this@OTPVerifyModify, "" + response.getString("message"), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@OTPVerifyModify, "" + response.getString("message"), Toast.LENGTH_SHORT).show()
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@OTPVerifyModify)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@OTPVerifyModify, LoginActivity::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                //                                        sendbtn.setEnabled(true);
            }
            stopAnim()
        }) { error -> Log.d("ashok", error.toString()) }
        MySingleton.getInstance(this@OTPVerifyModify).addToRequestQueue(jsonObjectRequest)
    }

    fun sumit(view: View?) {
        val otp = editText1!!.text.toString()
        if (otp.length == 6) {
            if (Url.firebaseOTP) {
                verifyfirebse(otp)
            } else {
                verifyotp(otp)
            }
            startAnim()
        } else {
            Toast.makeText(this, "please type 6 digit otp", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyfirebse(otp: String) {
        /*Intent intent = getIntent();
        intent.putExtra("code", otp);
        setResult(1111, intent);
        finish();*/
        verifyPhoneNumberWithCode(verificationId, otp)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    private fun verifyotp(otp: String) {
        /*if (!isOtpSent) {
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "in progress please wait", Toast.LENGTH_SHORT).show();

        }
        isOtpSent = true;*/
        val url = Url.baseurl + "/otp_verify"
        val data = JSONObject()
        try {
            data.put("userMobile", intent.getStringExtra("mobile"))
            data.put("otp", otp)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        startAnim()
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, data, { response ->
            try {
                val success = response.getBoolean("success")
                if (success) {
                    val token = response.getString("token")
                    val data1 = response.getJSONArray("data")
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@OTPVerifyModify)
                    val editor = preferences.edit()
                    editor.putBoolean(Datas.loginstatus, true)
                    val jsonObject = data1.getJSONObject(0)
                    editor.putInt(Datas.id, jsonObject.getInt(Datas.id))
                    if (jsonObject.getString(Datas.user_name) != "null") {
                        editor.putString(Datas.user_name, jsonObject.getString(Datas.user_name))
                    }
                    editor.putString(Datas.user_mobile, jsonObject.getString(Datas.user_mobile))
                    if (jsonObject.getString(Datas.user_age) != "null") editor.putString(Datas.user_age, jsonObject.getString(Datas.user_age))
                    if (jsonObject.getString(Datas.user_age) != "null") editor.putString(Datas.user_postal_code, jsonObject.getString(Datas.user_postal_code))
                    if (jsonObject.getString(Datas.user_age) != "null") editor.putString(Datas.user_state, jsonObject.getString(Datas.user_state))
                    if (jsonObject.getString(Datas.user_age) != "null") editor.putString(Datas.user_district, jsonObject.getString(Datas.user_district))
                    if (jsonObject.getString(Datas.user_age) != "null") editor.putString(Datas.lagnuage_id, jsonObject.getString(Datas.lagnuage_id))
                    editor.putString(Datas.token, token)
                    editor.apply()
                    startActivity(Intent(this@OTPVerifyModify, Register::class.java))
                    Toast.makeText(this@OTPVerifyModify, "" + response.getString("message"), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@OTPVerifyModify, "" + response.getString("message"), Toast.LENGTH_SHORT).show()
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@OTPVerifyModify)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@OTPVerifyModify, LoginActivity::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                sendbtn!!.isEnabled = true
            }
            stopAnim()
        }) { error ->
            sendbtn!!.isEnabled = true
            stopAnim()
            Toast.makeText(this@OTPVerifyModify, "" + error, Toast.LENGTH_SHORT).show()
        }
        //        requestQueue.add(jsonObjectRequest);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        sendbtn!!.isEnabled = false
    }

    override fun onStart() {
        super.onStart()
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
    }

    fun resend(view: View?) {
        startAnim()
        if (Url.firebaseOTP) {
            resendVerificationCode(intent.getStringExtra("mobile"), LoginActivity.token)
        } else {
            //// TODO: 18/1/21  
        }
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
    private fun resendVerificationCode(phoneNumber: String, token: ForceResendingToken) {
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
                .setPhoneNumber(phoneNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(mCallbacks!!) // OnVerificationStateChangedCallbacks
                .setForceResendingToken(token) // ForceResendingToken from callbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun back(view: View?) {
        val intent = intent
        setResult(1112, intent)
        finish()
    }

    companion object {
        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}