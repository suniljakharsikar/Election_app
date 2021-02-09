package b2d.l.mahtmagandhi

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import b2d.l.mahtmagandhi.Utility.customSnackBar
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_setting_profile.*
import kotlinx.android.synthetic.main.activity_setting_profile.button_submit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class SettingProfile : AppCompatActivity() {


    val langIds = arrayListOf<String>()
    private var resultUri: Uri? = null
    private lateinit var myCalendar: Calendar
    private var avi: ProgressBar? = null
    val languages = arrayListOf<String>()

    fun startAnim() {
        //avi!!.show()
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
        setContentView(R.layout.activity_setting_profile)
        avi = findViewById(R.id.avi2)

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        disableEdit()

        //        city.setText(preferences.getString(Datas.user_district, ""));
        editText_postal_code.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //if (s.length > 5 && s.length == 6) fetchLoc(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 5 && s.length == 6) fetchLoc(s.toString())
            }
        })
        iv_edit_avtar_setting.setOnClickListener {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setFixAspectRatio(true)

                    .start(this);

            // val dialog = ImagePickerBottomSheetDialogFragment()
            //   dialog.show(supportFragmentManager, "pic")
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(OnCompleteListener<String> { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    try {
                        val job = GlobalScope.async {
                            return@async Utility.isInternetAvailable()
                        }
                        job.invokeOnCompletion {
                            val isInternet = job.getCompleted()
                            if (isInternet) {
                                fetchData(task.result)

                            } else {
                                stopAnim()
                                customSnackBar(iv_edit_avtar_setting!!, this, "No Internet Available.", ContextCompat.getColor(this, R.color.error), R.drawable.ic_error)
                            }
                        }
                    } catch (e: Exception) {

                    }


                    // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                })


        var radioGroupSex = findViewById<RadioGroup>(R.id.radioGroup_gender)
        materialButton_edit_setting_profie.setOnClickListener {
            enableEdit()
        }


        val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        val camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (
                write == PackageManager.PERMISSION_GRANTED &&
                read == PackageManager.PERMISSION_GRANTED &&
                camera == PackageManager.PERMISSION_GRANTED
        ) {

        } else {


            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ), 120)
        }
    }

    private fun enableEdit() {
        materialButton_edit_setting_profie.visibility = View.GONE
        imageView43.visibility = View.GONE
        editTextPersonName2.isEnabled = true
        editTextPhone2.isEnabled = false
        editText_postal_code.isEnabled = true
        editText_dob_setting_profile.isEnabled = true
        editText_state.isEnabled = true
        editText_district.isEnabled = true
        actv_city_locality.isEnabled = true
        actv_lang_locality.isEnabled = true
        til_city_setting_profile.isEndIconVisible = true
        til_lang_profile.isEndIconVisible = true

        radioGroup_gender.isEnabled = true
        radioGroup_gender.visibility = View.VISIBLE
        textView_gender_setting_profile.visibility = View.GONE

        rb_male_setting_profile.isEnabled = true
        rb_female_setting_profile.isEnabled = true
        rb_other_setting_profile.isEnabled = true


        button_submit.visibility = View.VISIBLE
        // editTextPersonName2.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        //editTextPersonName2.setFilters(arrayOf(getEditTextFilter()))
        //editTextPhone2.inputType = InputType.TYPE_CLASS_PHONE
       // val digits = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" // or any characters you want to allow
       // editTextPersonName2.keyListener = DigitsKeyListener.getInstance(digits)
       // editTextPersonName2.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME

        //AppHelper.filterAlphabates(editTextPersonName2)
        val blankDigit = ""
        editTextPhone2.keyListener = DigitsKeyListener.getInstance(blankDigit)

        editText_postal_code.inputType = InputType.TYPE_CLASS_NUMBER
        //editText_dob_setting_profile.inputType = InputType.TYPE_CLASS_TEXT
        editText_state.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
        editText_district.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
        myCalendar = Calendar.getInstance()
        val date = OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        editText_dob_setting_profile.setOnClickListener(View.OnClickListener { /*new DatePickerDialog(Register.this, date,myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH) ).show();*/
            val datepickerdialog = DatePickerDialog(this@SettingProfile,
                    AlertDialog.THEME_HOLO_DARK, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -14)
            datepickerdialog.datePicker.maxDate = calendar.timeInMillis




            datepickerdialog.show()

        })
    }

    private fun disableEdit() {
        materialButton_edit_setting_profie.visibility = View.VISIBLE

       // editTextPersonName2.inputType = InputType.TYPE_NULL
        editTextPhone2.inputType = InputType.TYPE_NULL
        editText_postal_code.inputType = InputType.TYPE_NULL
        editText_dob_setting_profile.inputType = InputType.TYPE_NULL
        editText_state.inputType = InputType.TYPE_NULL
        editText_district.inputType = InputType.TYPE_NULL
        button_submit.visibility = View.INVISIBLE

        editTextPersonName2.isEnabled = false
        editTextPhone2.isEnabled = false
        editText_postal_code.isEnabled = false
        editText_dob_setting_profile.isEnabled = false
        editText_state.isEnabled = false
        editText_district.isEnabled = false
        actv_city_locality.isEnabled = false
        actv_lang_locality.isEnabled = false



        til_city_setting_profile.isEndIconVisible = false
        til_lang_profile.isEndIconVisible = false
        // til_lang_profile.isEnabled = false

        radioGroup_gender.isEnabled = false
        rb_male_setting_profile.isEnabled = false
        rb_female_setting_profile.isEnabled = false
        rb_other_setting_profile.isEnabled = false


        radioGroup_gender.visibility = View.GONE
        textView_gender_setting_profile.visibility = View.VISIBLE


    }

    private fun updateLabel() {
        val myFormat = "dd MMM yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editText_dob_setting_profile.setText(sdf.format(myCalendar.time))
    }

    private fun fetchData(token: String) {
        startAnim()
        // [START_EXCLUDE]
//                            updateUI(STATE_SIGNIN_SUCCESS, user);
//                            Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
        val url = Url.baseurl + "/users"
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@SettingProfile)

        val jsonrequest = JSONObject()
        try {
            jsonrequest.put("userMobile", preferences.getString(Datas.user_mobile, ""))
            jsonrequest.put("fcm_token", token)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonrequest, { response ->
            try {
                stopAnim()
                Log.d("Info", "fetchData: " + response)
                val success = response.getBoolean("success")
                if (success) {
                    stopAnim()
                    val token = response.getString("token")
                    val data1 = response.getJSONArray("data")
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
                    if (jsonObject.getString(Datas.gender) != "null") editor.putString(Datas.gender, jsonObject.getString(Datas.gender))

                    editor.putString(Datas.token, token)
                    editor.apply()
                    val img = jsonObject.getString(Datas.user_image)
                    if (!isFinishing()) {
                        if (img.contains("http")) Glide.with(this).load(img).into(profile_image)
                        else if (img == "null") Glide.with(this).load(R.drawable.ic_user_place_holder).into(profile_image)
                        else if (img.length > 0 && img != "null") Glide.with(this).load(Url.burl + img).into(profile_image)

                    }
                    editTextPersonName2.setText(preferences.getString(Datas.user_name, ""))
                    editTextPhone2.setText(preferences.getString(Datas.user_mobile, ""))
                    try {
                        val dob = preferences.getString(Datas.user_age, "")
                        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val dobp = simpleDateFormat.parse(dob)
                        val transformFormat = SimpleDateFormat("dd MMM yyyy")

                        editText_dob_setting_profile.setText(transformFormat.format(dobp))
                    } catch (e: Exception) {
                    }

                    editText_postal_code.setText(preferences.getString(Datas.user_postal_code, ""))
                    editText_state.setText(preferences.getString(Datas.user_state, ""))
                    editText_district.setText(preferences.getString(Datas.user_district, ""))
                    actv_city_locality.setText(preferences.getString(Datas.user_village, ""))
                    textView_gender_setting_profile.setText(preferences.getString(Datas.gender, ""))

                    when (preferences!!.getString(Datas.gender, "Male")) {
                        "Male" -> {
                            radioGroup_gender.check(R.id.rb_male_setting_profile)
                        }
                        "Female" -> {
                            radioGroup_gender.check(R.id.rb_female_setting_profile)
                        }
                        "Others" -> {
                            radioGroup_gender.check(R.id.rb_other_setting_profile)

                        }
                    }

                        setLanguage(preferences.getString(Datas.lagnuage_id, "")!!)
                        // Toast.makeText(this@SettingProfile, "" + response.getString("message"), Toast.LENGTH_SHORT).show()
                        //finish()
                    } else {
                        Toast.makeText(this@SettingProfile, "" + response.getString("message"), Toast.LENGTH_SHORT).show()
                        //login page
                        val preferences = PreferenceManager.getDefaultSharedPreferences(this@SettingProfile)
                        val editor = preferences.edit()
                        editor.clear()
                        editor.apply()
                        startActivity(Intent(this@SettingProfile, LoginActivity::class.java))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //                                        sendbtn.setEnabled(true);
                }
                stopAnim()
            }, { error ->

            stopAnim()
            customSnackBar(iv_edit_avtar_setting, this@SettingProfile, error.toString(),
                    ContextCompat.getColor(this@SettingProfile, R.color.error), R.drawable.ic_error)

        })
            MySingleton.getInstance(this@SettingProfile).addToRequestQueue(jsonObjectRequest)
        }

                private fun setLanguage(languageId: String) {

            val url = Url.baseurl + "/language_list"
            val jsonRequest = JSONObject()
            startAnim()
            val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
                stopAnim()
                try {
                    val success = response.getBoolean("success")
                    if (success) {

                        val data = response.getJSONArray("data")


                        for (i in 0 until data.length()) {
                            val jsonObject = data.getJSONObject(i)
                            //                            JsonObject jsonObject = (JsonObject) data.get(i);
                            languages.add(jsonObject["name"].toString())
                            langIds.add(jsonObject["id"].toString())
                            if (jsonObject["id"].toString() == languageId) {
                                actv_lang_locality.setText(jsonObject["name"].toString(), false)
                            }
                        }

                        val adapter = StringAdapter(this, languages)

                        actv_lang_locality.setAdapter(adapter)
                    } else {
                        Toast.makeText(this@SettingProfile, "" + response.getString("message"), Toast.LENGTH_SHORT).show()
                        //login page
                        val preferences = PreferenceManager.getDefaultSharedPreferences(this@SettingProfile)
                        val editor = preferences.edit()
                        editor.clear()
                        editor.apply()
                        startActivity(Intent(this@SettingProfile, LoginActivity::class.java))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                stopAnim()
            }, Response.ErrorListener { error ->
                Toast.makeText(this@SettingProfile, "" + error, Toast.LENGTH_SHORT).show()
                stopAnim()
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    var preferences = PreferenceManager.getDefaultSharedPreferences(this@SettingProfile)

                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["Token"] = preferences.getString(Datas.token, "")!!
                    return headers
                }
            }
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

        }

                private fun fetchLoc(s: String) {
            Utility.hideKeyboard(this)
            startAnim()
            val jsonObjectRequest = StringRequest(Request.Method.GET, "https://api.postalpincode.in/pincode/$s",
                    { response ->
                        stopAnim()
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
                                val adapter1 = StringAdapter(this@SettingProfile, ArrayList(cities))

                                actv_city_locality.setAdapter(adapter1)
                                if (cities.size > 0)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        if (actv_city_locality.text.length == 0)
                                            actv_city_locality.setText(cities.get(0), false)

                                    }
                            }
                        } catch (e: Exception) {
                        }
                    }) { error ->
                Log.d("Register", "onErrorResponse: $error")
                stopAnim()
            }
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
                    var s5: String = editText_dob_setting_profile.getText().toString()
                    if (Register.isNullOrEmpty(s5)) {
                        Toast.makeText(this, "Please type your age", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val s1: String = editText_postal_code.getText().toString()
                    if (Register.isNullOrEmpty(s1)) {
                        Toast.makeText(this, "Please enter valid pincode", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (s1.trim().length != 6) {
                        Toast.makeText(this, "Please enter valid pincode", Toast.LENGTH_SHORT).show()
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

                    val job = GlobalScope.async {
                        return@async Utility.isInternetAvailable()
                    }
                    job.invokeOnCompletion {
                        val isInternet = job.getCompleted()
                        GlobalScope.launch(Dispatchers.Main) {
                            if (isInternet) {
                                startAnim()
                                submitLang()

                                val url = Url.baseurl + "/update_profile"
                                val jsonRwquest = JSONObject()
                                val cityS: String
                                cityS = if (actv_city_locality.text == null) "" else actv_city_locality.text.toString()
                                try {
                                    val formatter = SimpleDateFormat("dd MMM yyyy")
                                    val date = formatter.parse(s5)
                                    val transFormatter = SimpleDateFormat("yyyy-MM-dd")
                                    s5 = transFormatter.format(date)
                                } catch (e: ParseException) {
                                    //e.printStackTrace();
                                }
                                try {
                                    jsonRwquest.put("userMobile", editTextPhone2.getText().toString())
                                    jsonRwquest.put("userName", s)

                                    try {
                                        val r = findViewById<RadioButton>(radioGroup_gender.checkedRadioButtonId)
                                        jsonRwquest.put("gender", r.text.toString())
                                    } catch (e: Exception) {
                                    }
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
                                    stopAnim()
                                    try {
                                        val success = response.getBoolean("success")

                                        if (success) {
                                            //newposting
                                            Utility.customSnackBar(editTextPersonName2, this@SettingProfile, "Successfully Updated", ContextCompat.getColor(this@SettingProfile, R.color.success), R.drawable.ic_success)
                                            disableEdit()
                                            FirebaseMessaging.getInstance().getToken()
                                                    .addOnCompleteListener(OnCompleteListener<String> { task ->
                                                        if (!task.isSuccessful) {
                                                            return@OnCompleteListener
                                                        }

                                                        // Get new FCM registration token
                                                        try {
                                                            fetchData(task.result)
                                                        } catch (e: Exception) {

                                                        }


                                                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    })

                                        } else {
                                            Utility.customSnackBar(editTextPersonName2, this@SettingProfile, "" + response.getString("message"), ContextCompat.getColor(this@SettingProfile, R.color.error), R.drawable.ic_error)

                                        }
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    // stopAnim()
                                }, Response.ErrorListener { error ->
                                    stopAnim()
                                    Toast.makeText(this@SettingProfile, "e= $error", Toast.LENGTH_SHORT).show()
                                }) {
                                    override fun getHeaders(): Map<String, String> {
                                        val headers = HashMap<String, String>()
                                        headers["Content-Type"] = "application/json"
                                        var preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)

                                        headers["Token"] = preferences.getString(Datas.token, "").toString()
                                        return headers
                                    }
                                }
                                MySingleton.getInstance(this@SettingProfile).addToRequestQueue(jsonObjectRequest)
                            } else {
                                stopAnim()
                                customSnackBar(editTextPersonName2!!, this@SettingProfile, "No Internet Available.", ContextCompat.getColor(this@SettingProfile, R.color.error), R.drawable.ic_error)
                            }
                        }
                    }

                }


                        private fun dispatchTakePictureIntent(requestCode: Int) {

                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                        // Ensure that there's a camera activity to handle the intent

                        (packageManager)?.also {
                            // Create the File where the photo should go
                            val photoFile: File? = try {
                                createImageFile()
                            } catch (ex: IOException) {
                                // Error occurred while creating the File

                                null
                            }
                            // Continue only if the File was successfully created
                            photoFile?.also {
                                val photoURI: Uri = FileProvider.getUriForFile(
                                        this,
                                        "b2d.l.mahtmagandhi.fileprovider",
                                        it
                                )


                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                startActivityForResult(takePictureIntent, requestCode)


                            }


                        }
                    }

                }


                        private
                        var currentPath: String = ""
                                private
                                val REQUEST_TAKE_GPHOTO: Int = 51
                                        private
                                        val REQUEST_TAKE_PHOTO: Int = 50
                var currentPhotoPath : String = ""

        @Throws(IOException::class)
        private fun createImageFile(): File {
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            return File.createTempFile(
                    "JPEG_${timeStamp}_", /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
        }


        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    resultUri = result!!.getUri();
                    if (!isFinishing())
                    Glide.with(this).load(resultUri).into(profile_image)
                    newposting()

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result!!.getError();
                }
            } else if (resultCode.equals(Activity.RESULT_OK)) {

                var uri = data?.data


                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    // Do something for lollipop and above versions
                    val file = File(uri!!.path) //create path from uri
                     if (file.path.contains(":")) {
                         val split = file.path.split(":".toRegex()).toTypedArray() //split the path.

                         currentPhotoPath = split[1]
                     }else{
                         currentPhotoPath =file.path
                     }
                } else{*/
                // do something for phones running an SDK before lollipop
                if (uri != null)
                    currentPhotoPath = PathUtil.getPath(this, uri)

                var result: Bitmap? = BitmapFactory.decodeFile(currentPhotoPath)
                if (result == null) {
                    val file = File(uri!!.path) //create path from uri
                    if (file.path.contains(":")) {
                        val split = file.path.split(":".toRegex()).toTypedArray() //split the path.

                        currentPhotoPath = split[1]
                    } else currentPhotoPath = file.absolutePath
                    result = BitmapFactory.decodeFile(currentPhotoPath)
                    if (result == null) {
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                        val cursor: Cursor? = getContentResolver()?.query(
                                uri,
                                filePathColumn, null, null, null
                        )
                        cursor?.moveToFirst()

                        val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
                        currentPhotoPath = columnIndex?.let { cursor?.getString(it) }.toString()
                        cursor?.close()
                    }

                    result = BitmapFactory.decodeFile(currentPhotoPath)


                }

                //   BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?
                //}


            }
            //setPic(requestCode)


        }


        fun getPic(tag: String?, s: String) {
            if (s.equals("c")) {
                when (tag) {
                    "pic" -> dispatchTakePictureIntent(REQUEST_TAKE_PHOTO)

                }
            } else {
                when (tag) {
                    "pic" -> dispatchTakeGalleryPictureIntent(REQUEST_TAKE_GPHOTO)

                }
            }

        }

        val REQUEST_IMAGE_CAPTURE = 1


        private fun dispatchTakeGalleryPictureIntent(requestTakePhotoCode: Int) {
            startActivityForResult(
                    Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                    ), requestTakePhotoCode
            )

        }


        fun newposting() {

//        Toast.makeText(this, "new posting", Toast.LENGTH_SHORT).show()
            startAnim()


            val client = OkHttpClient().newBuilder()
                    .build()
            val mediaType = MediaType.parse("text/plain")
            val bodyp = MultipartBody.Builder().setType(MultipartBody.FORM)

            if (resultUri != null) {
                bodyp.addFormDataPart("profileImage", "profilej.jpg",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                resultUri!!.toFile()))


            }

            val body = bodyp.build()
            val preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)

            val request: okhttp3.Request = okhttp3.Request.Builder()
                    .url(Url.baseurl + "/profile_image")
                    .method("POST", body)
                    .addHeader("token", preferences.getString(Datas.token, "")!!)
                    .addHeader("lid", preferences.getString(Datas.lagnuage_id, "1")!!)
                    .addHeader("Content-Type", "application/json")

                    .build()
            val job = GlobalScope.async {
                val response = client.newCall(request).execute()
                Log.d("NewPost", "newposting: " + response.isSuccessful)
                if (response.isSuccessful) {
                    GlobalScope.launch(Dispatchers.Main) {
                        // profile_image.setImageDrawable(null)

                        customSnackBar(editText_postal_code, this@SettingProfile, "Successfully profile uploaded", ContextCompat.getColor(this@SettingProfile, R.color.success), R.drawable.ic_success)
                        stopAnim()

                    }
                }
                stopAnim()
            }

        }


        fun submitLang() {

            val url = Url.baseurl + "/language_update"
            val jsonRequest = JSONObject()
            val actvLang = actv_lang_locality.text.toString()

            var index = ""
            for (i in 0..(languages.size - 1)) {
                if (languages.get(i).contentEquals(actvLang)) {
                    index = langIds.get(i)
                }
            }




            try {
                jsonRequest.put("langId", index)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            startAnim()
            val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonRequest, Response.Listener { response ->
                try {
                    val success = response.getBoolean("success")
                    if (success) {
                        val preferences = PreferenceManager.getDefaultSharedPreferences(this@SettingProfile)
                        val editor = preferences.edit()
                        editor.putBoolean(Datas.language_selected, true)
                        editor.putString(Datas.lagnuage_id, index)
                        editor.apply()
                        startActivity(Intent(this@SettingProfile, Home::class.java))
                        finish()
                    } else {
                        // Toast.makeText(Language.this, "" + response.getString("message"), // Toast.LENGTH_SHORT).show();
                        //login page
                        val preferences = PreferenceManager.getDefaultSharedPreferences(this@SettingProfile)
                        val editor = preferences.edit()
                        editor.clear()
                        editor.apply()
                        startActivity(Intent(this@SettingProfile, LoginActivity::class.java))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                stopAnim()
            }, Response.ErrorListener {
                stopAnim()
                // Toast.makeText(Language.this, "" + error, // Toast.LENGTH_SHORT).show();
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    var preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
                    headers["token"] = preferences!!.getString(Datas.token, "")!!
                    return headers
                }
            }
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }


    }

