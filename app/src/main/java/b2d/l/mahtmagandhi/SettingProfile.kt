package b2d.l.mahtmagandhi

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_setting_profile.*
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
        button2.visibility = View.INVISIBLE

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

            button2.visibility = View.VISIBLE
            editTextPersonName2.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            //editTextPhone2.inputType = InputType.TYPE_CLASS_PHONE
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

    fun back(view: View?) {
        finish()
    }
}