package b2d.l.mahtmagandhi

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import b2d.l.mahtmagandhi.Register
import b2d.l.mahtmagandhi.Utility.customSnackBar
import b2d.l.mahtmagandhi.Utility.hideKeyboard
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_setting_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Register : AppCompatActivity() {
    var name: EditText? = null
    var number: EditText? = null
    var age: EditText? = null
    var pincode: EditText? = null
    var state: EditText? = null
    var distirct: EditText? = null
    var city: AutoCompleteTextView? = null
    private var preferences: SharedPreferences? = null
    private var avi: ProgressBar? = null
    private var myCalendar: Calendar? = null
    var radioGroup: RadioGroup? = null
    fun startAnim() {
        // avi.show();
        avi!!.visibility = View.VISIBLE
        // or avi.smoothToShow();
    }

    fun stopAnim() {
        avi!!.visibility = View.INVISIBLE
        //        avi.hide();
        // or avi.smoothToHide();
    }

    override fun onStart() {
        super.onStart()
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val loginstatus = preferences.getBoolean(Datas.registration, false)
        if (loginstatus) {
            // FIXME: 08-02-2021  
           fetchLanguage()
            //inish()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        avi = findViewById(R.id.avi3)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        name = findViewById(R.id.editTextPersonName)
        number = findViewById(R.id.editTextPhone)
        age = findViewById(R.id.editText_dob)
        pincode = findViewById(R.id.editText_postal_code)
        state = findViewById(R.id.editText_state)
        distirct = findViewById(R.id.editText_district)
        city = findViewById(R.id.actv_city_locality)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        name!!.setText(preferences!!.getString(Datas.user_name, ""))
        number!!.setText(preferences!!.getString(Datas.user_mobile, ""))
        number!!.isEnabled = false
        //AppHelper.filterAlphabates(name)

        try {
            val dob = preferences!!.getString(Datas.user_age, "")
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val dobp = simpleDateFormat.parse(dob)
            val transformFormat = SimpleDateFormat("dd MMM yyyy")

            age!!.setText(transformFormat.format(dobp))
        } catch (e: Exception) {
        }
        pincode!!.setText(preferences!!.getString(Datas.user_postal_code, ""))
        if (preferences!!.getString(Datas.user_postal_code, "")!!.length == 6) fetchLoc(preferences!!.getString(Datas.user_postal_code, ""))
        state!!.setText(preferences!!.getString(Datas.user_state, ""))
        if (!Url.firebaseOTP) {
            state!!.setEnabled(false)
            distirct!!.setEnabled(false)
        }
        distirct!!.setText(preferences!!.getString(Datas.user_district, ""))
        city!!.setText(preferences!!.getString(Datas.user_village, ""), false)

        radioGroup = findViewById(R.id.radioGroup_gender)

        when(preferences!!.getString(Datas.gender,"Male")){
            "Male"->{
                radioGroup_gender.check(R.id.rb_male_regi)
            }
            "Female"->{
                radioGroup_gender.check(R.id.rb_female_regi)
            }
            "Others"->{
                radioGroup_gender.check(R.id.rb_others)

            }

        }        //        city.setText(preferences.getString(Datas.user_village, ""));
        stopAnim()
        myCalendar = Calendar.getInstance()
        val date = OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar!!.set(Calendar.YEAR, year)
            myCalendar!!.set(Calendar.MONTH, monthOfYear)
            myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        age!!.setOnClickListener(View.OnClickListener { /*new DatePickerDialog(Register.this, date,myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH) ).show();*/
            val datepickerdialog = DatePickerDialog(this@Register,
                    AlertDialog.THEME_HOLO_DARK, date, myCalendar!!
                    .get(Calendar.YEAR), myCalendar!!.get(Calendar.MONTH),
                    myCalendar!!.get(Calendar.DAY_OF_MONTH))
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -14)
            datepickerdialog.datePicker.maxDate = calendar.timeInMillis
            datepickerdialog.show()

        })
        pincode!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 5 && s.length == 6 && !Url.firebaseOTP) fetchLoc(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 5 && s.length == 6 && !Url.firebaseOTP) fetchLoc(s.toString() )
            }
        }
        )
        /*age.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Register.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/
    }

    private fun updateLabel() {
        val myFormat = "dd MMM yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        age!!.setText(sdf.format(myCalendar!!.time))
    }

    fun back(view: View?) {
        finish()
    }

    fun submit(view: View?) {
        val job = GlobalScope.async {
            return@async Utility.isInternetAvailable()
        }
        job.invokeOnCompletion {

                val isInternet = job.getCompleted()
            GlobalScope.launch(Dispatchers.Main) {
                if (isInternet) {
                    call()

                } else {
                    stopAnim()
                    customSnackBar(name!!, this@Register, "No Internet Available.", ContextCompat.getColor(this@Register, R.color.error), R.drawable.ic_error)
                }
            }

        }
        //        call1();
    }

    private fun call() {
        val s = name!!.text.toString()
        if (isNullOrEmpty(s)) {
            Toast.makeText(this, "Please type name", Toast.LENGTH_SHORT).show()
            return
        }
        var s5 = age!!.text.toString()
        if (isNullOrEmpty(s5)) {
            Toast.makeText(this, "Please type your Date of birth", Toast.LENGTH_SHORT).show()
            return
        }
        val s1 = pincode!!.text.toString()
        if (isNullOrEmpty(s1)) {
            Toast.makeText(this, "Please enter valid pincode", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Url.firebaseOTP && s1.trim().length!=6) {
            Toast.makeText(this, "Please enter valid pincode.", Toast.LENGTH_SHORT).show()
            return
        }
        val s3 = state!!.text.toString()
        if (isNullOrEmpty(s3)) {
            Toast.makeText(this, "Please type state name", Toast.LENGTH_SHORT).show()
            return
        }
        val s4 = distirct!!.text.toString()
        if (isNullOrEmpty(s4)) {
            Toast.makeText(this, "Please type district name", Toast.LENGTH_SHORT).show()
            return
        }
        val url = Url.baseurl + "/update_profile"
        val jsonRwquest = JSONObject()
        val cityS: String
        cityS = if (city!!.text == null) "" else city!!.text.toString()
        val id = radioGroup!!.checkedRadioButtonId
        val radioButton = findViewById<RadioButton>(id)
        val gender = radioButton.text.toString()
        try {
            val formatter = SimpleDateFormat("dd MMM yyyy")
            val date = formatter.parse(s5)
            val transFormatter = SimpleDateFormat("yyyy-MM-dd")
            s5 = transFormatter.format(date)
        } catch (e: ParseException) {
            //e.printStackTrace();
        }
        try {
            jsonRwquest.put("userMobile", number!!.text.toString())
            jsonRwquest.put("userName", s)
            jsonRwquest.put("userAge", s5)
            jsonRwquest.put("userPostalCode", s1)
            jsonRwquest.put("userState", s3)
            jsonRwquest.put("userDistrict", s4)
            jsonRwquest.put("userVillage", cityS)
            jsonRwquest.put("gender", gender)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRwquest, Response.Listener { response ->
            try {
                val success = response.getBoolean("success")
                Toast.makeText(this@Register, "" + response.getString("message"), Toast.LENGTH_SHORT).show()
                if (success) {
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Register)
                    val editor = preferences.edit()
                    editor.putBoolean(Datas.registration, true)
                    editor.putString(Datas.gender,gender)
                    editor.apply()

                    fetchLanguage()
                }else if (response!!.getInt("status_code")==403){
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Register)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()


                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            stopAnim()
        }, Response.ErrorListener { error ->
            error.printStackTrace()
            stopAnim()
            customSnackBar(city!!, this@Register, error.toString(),
                    ContextCompat.getColor(this@Register, R.color.error), R.drawable.ic_error)
        }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Token"] = preferences!!.getString(Datas.token, "")!!
                Log.d("token=", preferences!!.getString(Datas.token, ""))
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    private fun fetchLanguage() {
        val url = Url.baseurl + "/language_list"
        val jsonRequest = JSONObject()
        startAnim()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
            try {
                val gson = Gson()
                val (data, _, _, success) = gson.fromJson(response.toString(), LanguageResponseModel::class.java)
                if (success) {

                    if (data.size>1) {
                        startActivity(Intent(this@Register, Language::class.java))
                        finish()
                    }else if(data.size==1){
                        val preferences = PreferenceManager.getDefaultSharedPreferences(this@Register)
                        val editor = preferences.edit()
                        editor.putBoolean(Datas.language_selected, true)
                        editor.putString(Datas.lagnuage_id, data.get(0).id.toString())
                        editor.apply()
                        startActivity(Intent(this@Register, Home::class.java))
                        finish()
                    }

                } else {
                    // Toast.makeText(Language.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                    //login page
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Register)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this@Register, LoginActivity::class.java))
                    finish()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            stopAnim()
        }, Response.ErrorListener { error ->

            stopAnim()
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Token"] = preferences!!.getString(Datas.token, "")!!
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun fetchLoc(s: String?) {
        hideKeyboard(this)
        startAnim()
        val stringRequest = StringRequest(Request.Method.GET, "https://api.postalpincode.in/pincode/$s", { response ->
            stopAnim()
            val gson = Gson()
            val pinCodeResponseModelItems = gson.fromJson(response, PinCodeResponseModel::class.java)
            try {
                if (pinCodeResponseModelItems != null && pinCodeResponseModelItems.size > 0) {
                    val (_, postOffice) = pinCodeResponseModelItems[0]
                    if (postOffice.size > 0) {
                        val (_, _, _, _, _, _, district, _, _, _, _, state1) = postOffice[0]
                        state!!.setText(state1)
                        distirct!!.setText(district)
                        val cities: MutableList<String> = ArrayList()
                        for ((_, _, _, _, _, _, _, _, name1) in postOffice) {
                            cities.add(name1)
                        }
                        val adapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, cities)
                        val adapter1 = StringAdapter(this@Register, ArrayList(cities))
                        city!!.setAdapter(adapter1)
                        city!!.setText("")
                        if (cities.size > 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (city!!.text.toString().length == 0) city!!.setText(cities[0], false)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }) { }
        MySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
    }

    companion object {

        fun isNullOrEmpty(str: String?): Boolean {
            return str == null || str.isEmpty()
        }
    }
}