package b2d.l.mahtmagandhi

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
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
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.activity_setting_profile.*
import kotlinx.android.synthetic.main.activity_setting_profile.button_submit
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SettingProfile : AppCompatActivity() {


    private var avi: AVLoadingIndicatorView? = null

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
        setContentView(R.layout.activity_setting_profile)
        avi = findViewById(R.id.avi2)

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        editTextPersonName2.inputType = InputType.TYPE_NULL
        editTextPhone2.inputType = InputType.TYPE_NULL
        editText_postal_code.inputType = InputType.TYPE_NULL
        editText_dob_setting_profile.inputType = InputType.TYPE_NULL
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
        iv_edit_avtar_setting.setOnClickListener {
            val dialog = ImagePickerBottomSheetDialogFragment()
            dialog.show(supportFragmentManager, "pic")
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(OnCompleteListener<String> { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token

                    fetchData(task.result)


                    // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                })


        var radioGroupSex = findViewById<RadioGroup>(R.id.radioGroup)
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
                /* val d: DialogFragment = DatePickerFragment(editText_dob_setting_profile)
                 d.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme)

                 d.show(supportFragmentManager, "date")*/
                val dp = MaterialDatePicker.Builder.datePicker().build()
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
                    editor.putString(Datas.token, token)
                    editor.apply()
                    editTextPersonName2.setText(preferences.getString(Datas.user_name, ""))
                    editTextPhone2.setText(preferences.getString(Datas.user_mobile, ""))
                    editText_dob_setting_profile.setText(preferences.getString(Datas.user_age, ""))
                    editText_postal_code.setText(preferences.getString(Datas.user_postal_code, ""))
                    editText_state.setText(preferences.getString(Datas.user_state, ""))
                    editText_district.setText(preferences.getString(Datas.user_district, ""))
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
            Log.d("ashok", error.toString())
            stopAnim()
        })
        MySingleton.getInstance(this@SettingProfile).addToRequestQueue(jsonObjectRequest)
    }

    private fun fetchLoc(s: String) {
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
                            actv_city_locality.setAdapter(ArrayAdapter(baseContext, android.R.layout.simple_dropdown_item_1line, cities))
                            if (cities.size > 0)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
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
        startAnim()
        val url = Url.baseurl + "/update_profile"
        val jsonRwquest = JSONObject()
        val cityS: String
        cityS = if (actv_city_locality.text == null) "" else actv_city_locality.text.toString()
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
            stopAnim()
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

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }

        val file = File(currentPhotoPath)
        MediaScannerConnection.scanFile(this, arrayOf(file.toString()),
                arrayOf(file.getName()), null)
    }

    private var currentPath: String = ""
    private val REQUEST_TAKE_GPHOTO: Int = 51
    private val REQUEST_TAKE_PHOTO: Int = 50
    var currentPhotoPath: String = ""

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
        if (resultCode.equals(Activity.RESULT_OK)) {

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
        setPic(requestCode)


    }

    private fun setPic(requestCode: Int) {
        // Get the dimensions of the View
        var imageView: ImageView = profile_image

        val targetW: Int = imageView.width
        val targetH: Int = imageView.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(currentPhotoPath, this)

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->

            imageView.setImageBitmap(bitmap)
            when (requestCode) {

                REQUEST_TAKE_GPHOTO -> {


                    currentPath = currentPhotoPath
                }

            }

            galleryAddPic()


        }
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

    private fun sdispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun dispatchTakeGalleryPictureIntent(requestTakePhotoCode: Int) {
        startActivityForResult(
                Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ), requestTakePhotoCode
        )

    }

}