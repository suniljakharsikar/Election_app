package b2d.l.mahtmagandhi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OTPVerify extends AppCompatActivity {

    private EditText editText1, editText2, editText3, editText4, editText5, editText6;
    private EditText[] editTexts;
    Button sendbtn;
    boolean isOtpSent = false;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        avi = findViewById(R.id.avi);

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
            verifyotp(otp);
//            verifyfirebse(otp);
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


    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
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
        finish();
    }
}