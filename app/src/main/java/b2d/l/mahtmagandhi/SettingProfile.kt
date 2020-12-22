package b2d.l.mahtmagandhi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.*
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_setting_profile.*
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SettingProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_profile)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        editTextPersonName2.inputType = InputType.TYPE_NULL
        editTextPhone2.inputType = InputType.TYPE_NULL
        editText_postal_code.inputType = InputType.TYPE_NULL
        editText_dob_setting_profile.inputType = InputType.TYPE_NULL
        editText_state.inputType = InputType.TYPE_NULL
        editText_state.inputType = InputType.TYPE_NULL
        editText_district.inputType = InputType.TYPE_NULL
        button_submit.visibility = View.INVISIBLE



        //        city.setText(preferences.getString(Datas.user_district, ""));
        editText_postal_code.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 5 && s.length == 6) fetchLoc(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 5 && s.length == 6) fetchLoc(s.toString())
            }
        })
        tv_edit_profile.setOnClickListener {

            button_submit.visibility = View.VISIBLE
           // editTextPersonName2.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            //editTextPersonName2.setFilters(arrayOf(getEditTextFilter()))
            //editTextPhone2.inputType = InputType.TYPE_CLASS_PHONE
            val digits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" // or any characters you want to allow
            editTextPersonName2.keyListener = DigitsKeyListener.getInstance(digits)
            editTextPersonName2.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME

            val blankDigit = ""
            editTextPhone2.keyListener = DigitsKeyListener.getInstance(blankDigit)

            editText_postal_code.inputType = InputType.TYPE_CLASS_NUMBER
            //editText_dob_setting_profile.inputType = InputType.TYPE_CLASS_TEXT
            editText_state.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
            editText_district.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
            editText_dob_setting_profile.setOnClickListener {
              val dp =   MaterialDatePicker.Builder.datePicker().build()
                dp.addOnPositiveButtonClickListener {
                    val date = Date(it)
                    val formatter: DateFormat = SimpleDateFormat("dd MMM yyyy")
                    //formatter.setTimeZone(TimeZone.getTimeZone(""))
                    val dateFormatted: String = formatter.format(date)
                    editText_dob_setting_profile.setText(dateFormatted)
                }
                dp.show(supportFragmentManager, "date")
            }
        }
    }
    private fun fetchLoc(s: String) {
        val jsonObjectRequest = StringRequest(Request.Method.GET, "https://api.postalpincode.in/pincode/$s",
                { response ->
                    Log.d("Register", "onResponse: $response")
                    val gson = Gson()
                    val pinCodeResponseModelItems = gson.fromJson(response, PinCodeResponseModel::class.java)
                    try {
                        if (pinCodeResponseModelItems != null && pinCodeResponseModelItems.size > 0) {
                            val (_, postOffice) = pinCodeResponseModelItems[0]
                            val (_, _, _, _, _, _, district, _, _, _, _, state) = postOffice[0]
                            editText_state.setText(state)
                            editText_district.setText(district)
                            val cities: MutableList<String> = ArrayList()
                            for (i in postOffice) {
                                cities.add(i.name)
                            }
                            spinner_city_setting_profile.setAdapter(ArrayAdapter(baseContext, android.R.layout.simple_dropdown_item_1line, cities))
                        }
                    } catch (e: Exception) {
                    }
                }) { error -> Log.d("Register", "onErrorResponse: $error") }
        MySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest)
    }



    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }



    fun back(view: View?) {
        finish()
    }

    fun update(view: View) {
        val s: String = editTextPersonName2.getText().toString()
        if (Register.isNullOrEmpty(s)) {
            Toast.makeText(this, "Please type name", Toast.LENGTH_SHORT).show()
            return
        }
        val s5: String = editText_dob_setting_profile.getText().toString()
        if (Register.isNullOrEmpty(s5)) {
            Toast.makeText(this, "Please type your age", Toast.LENGTH_SHORT).show()
            return
        }
        val s1: String = editText_postal_code.getText().toString()
        if (Register.isNullOrEmpty(s1)) {
            Toast.makeText(this, "Please type pincode", Toast.LENGTH_SHORT).show()
            return
        }
        val s3: String = editText_state.getText().toString()
        if (Register.isNullOrEmpty(s3)) {
            Toast.makeText(this, "Please type state name", Toast.LENGTH_SHORT).show()
            return
        }
        val s4: String = editText_district.getText().toString()
        if (Register.isNullOrEmpty(s4)) {
            Toast.makeText(this, "Please type district name", Toast.LENGTH_SHORT).show()
            return
        }
        val url = Url.baseurl + "/update_profile"
        val jsonRwquest = JSONObject()
        val cityS: String
        cityS = if (spinner_city_setting_profile.getSelectedItem() == null) "" else spinner_city_setting_profile.getSelectedItem().toString()
        try {
            jsonRwquest.put("userMobile", editTextPhone2.getText().toString())
            jsonRwquest.put("userName", s)
            //val x = s5.toInt()
            jsonRwquest.put("userAge", s5)
            jsonRwquest.put("userPostalCode", s1)
            jsonRwquest.put("userState", s3)
            jsonRwquest.put("userDistrict", s4)
            jsonRwquest.put("userVillage", cityS)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        //startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRwquest, Response.Listener { response ->
            try {
                val success = response.getBoolean("success")
                Toast.makeText(this@SettingProfile, "" + response.getString("message"), Toast.LENGTH_SHORT).show()
                if (success) {
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@SettingProfile)
                    val editor = preferences.edit()
                    editor.putBoolean(Datas.registration, true)
                    editor.apply()
                    startActivity(Intent(this@SettingProfile, Language::class.java))
                    finish()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            // stopAnim()
        }, Response.ErrorListener { error ->
            error.printStackTrace()
            // stopAnim()
            Toast.makeText(this@SettingProfile, "e= $error", Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                var preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)

                headers["Token"] = preferences.getString(Datas.token, "").toString()
                return headers
            } /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> x = new HashMap<>();
                x.put("userMobile", number.getText().toString());
                x.put("userName", name.getText().toString());
                x.put("userAge", age.getText().toString());
                x.put("userPostalCode", pincode.getText().toString());
                x.put("userState", state.getText().toString());
                x.put("userDistrict", distirct.getText().toString());
                return x;
            }*/
        } /*{
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", preferences.getString(Datas.token, ""));
                return params;

               */
        /* Map<String, String> params = new HashMap<String, String>();
                params.put("'Content-Type", "application/json");
                params.put("'Token", preferences.getString(Datas.token, ""));
                Log.d("ashok", "get header call");

                return params;*/
        /*
            }
        }*/
        //        requestQueue.add(jsonObjectRequest);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}