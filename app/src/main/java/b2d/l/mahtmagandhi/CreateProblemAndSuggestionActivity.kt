package b2d.l.mahtmagandhi

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_create_problem_and_suggestion.*
import kotlinx.android.synthetic.main.activity_create_problem_and_suggestion.et_prob_sugg
import kotlinx.android.synthetic.main.activity_create_problem_and_suggestion.tv_image_btn_prob_sug
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.fragment_image_picker_bottom_sheet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.File
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class CreateProblemAndSuggestionActivity : AppCompatActivity() {

    private  var imageAdapter: ImagesRecyclerViewAdapter? = null
    val images = arrayListOf<String>()
    private var avi: AVLoadingIndicatorView? = null
    val list = mutableListOf<Uri>()

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
        setContentView(R.layout.activity_create_problem_and_suggestion)
        avi = avi5
        stopAnim()
        imageAdapter = ImagesRecyclerViewAdapter(list)
        // Glide.with(this).load(data!!.getClipData()!!.getItemAt(0).getUri()).into(imageView_new_post)
        recyclerView_imgs_problem.adapter = imageAdapter

        val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (
                write == PackageManager.PERMISSION_GRANTED &&
                read == PackageManager.PERMISSION_GRANTED
        ){

        }
        else {


            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ), PERMISSION_REQUEST_CODE)

        }
        tv_image_btn_prob_sug.setOnClickListener {
            val dialog = ImagePickerBottomSheetDialogFragment()
            dialog.show(supportFragmentManager, "pic")
        }

        recyclerView_imgs_problem.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

    }

    fun back(view: View) {
        finish()
    }

    fun isNullOrEmpty(str: String?): Boolean {
        return if (str != null && !str.isEmpty()) false else true
    }

    fun submit(view: View) {

        if (et_prob_sugg.text.isEmpty()) Toast.makeText(this, "Please write your problem and suggestion.", Toast.LENGTH_SHORT).show()
        else if (et_prob_sugg_title.text.isEmpty()) Toast.makeText(this, "Please write your title.", Toast.LENGTH_SHORT).show()

        else {
            startAnim()
            val url = Url.baseurl + "/psuggestion_post"

            val client: OkHttpClient = OkHttpClient().newBuilder()
                    .build()
            val mediaType: MediaType = MediaType.parse("text/plain")!!
            val bodyP = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("title", et_prob_sugg_title.text.toString())
                    .addFormDataPart("descritpion", et_prob_sugg.text.toString());

            var counter = 0
            for (i in imageAdapter!!.imagesEncodedList){
                        bodyP.addFormDataPart("postImage[" + counter + "]", "p.jpg", RequestBody.create(MediaType.parse("application/octet-stream"), File(Utility.getPath(i,this)!!)))
                        counter = counter +1 ;
            }

                  val body =   bodyP.build()
            val preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)

            val request: Request = Request.Builder()
                    .url("https://election.suniljakhar.in/api/psuggestion_post")
                    .method("POST", body)
                    .addHeader("token", preferences.getString(Datas.token, "")!!)
                    .addHeader("lid", preferences.getString(Datas.lagnuage_id, "1")!!)
                    .addHeader("Content-Type", "application/json")

                    .build()
          GlobalScope.async {
              val response: Response= client.newCall(request).execute()

              if (response.isSuccessful){
                  GlobalScope.launch(Dispatchers.Main) {
                      Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                      et_prob_sugg_title.setText("")
                      et_prob_sugg.setText("")
                      images.clear()
                      imageView_pic_create_prob.setImageDrawable(null)
                      textView_tag_img_select.text = "Image Selected : 0"
                      stopAnim()
                      finish()

                  }
              }




          }




    /*        val multipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(Request.Method.POST, url, object : Response.Listener<NetworkResponse> {


                override fun onResponse(response: NetworkResponse?) {
                    Log.d("Response", "onResponse: " + response!!.data)
                    images.clear()
                    textView_tag_img_select.text = "Image Selected : 0"
                    et_prob_sugg_title.setText("")
                    et_prob_sugg.setText("")
                }
            }, object : Response.ErrorListener {


                override fun onErrorResponse(error: VolleyError?) {
                    Log.d("Response", "erroronResponse: " + error!!.networkResponse.data)
                    Toast.makeText(baseContext, error.localizedMessage, Toast.LENGTH_SHORT).show()

                }
            }) {
                override fun getHeaders(): MutableMap<String, String> {

                    val preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
                    val header: MutableMap<String, String> = java.util.HashMap()
                    header["Content-Type"] = "application/json"
                    header["token"] = preferences.getString(Datas.token, "")!!
                    header["lid"] = preferences.getString(Datas.lagnuage_id, "1")!!

                    return header


                }

                override fun getParams(): MutableMap<String, String> {
                    val params: MutableMap<String, String> = mutableMapOf()

                    params.put("title", et_prob_sugg_title.text.toString())
                    params.put("descritpion", et_prob_sugg.text.toString())
                return params
                }

                override fun getByteData(): Map<String, VolleyMultipartRequest.DataPart> {
                    val params = HashMap<String, VolleyMultipartRequest.DataPart>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    var forcounter = 0
                    for (i in images){
                        params.put("postImage[" + forcounter + "]", DataPart("p.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), i.getDrawable()), "image/jpeg"));
                        forcounter = forcounter+1
                    }

                    return params;
                }
            };

            MySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
*/

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

    private var count_dismiss: Int = 0
    private val PERMISSION_REQUEST_CODE: Int = 50
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
        if(requestCode == REQUEST_TAKE_GPHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                if (data?.getClipData() != null) {
                    var count = data!!.getClipData()!!.getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    Toast.makeText(this, "" + count + "", Toast.LENGTH_SHORT).show()

                    for (i in 0..count - 1) {
                        val imageUri = data!!.getClipData()!!.getItemAt(i).getUri();
                        list.add(imageUri)
                    }
                    imageAdapter?.notifyDataSetChanged()

                    //do something with the image (save it to some directory or whatever you need to do with it here)
                } else if (data?.getData() != null) {
                    val imagePath = data.getData()!!.getPath();


                    try {
                        list.add(data.data!!)
                    } catch (e: Exception) {
                    }
                    imageAdapter?.notifyDataSetChanged()

                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }

            }
        }
        else if (resultCode.equals(Activity.RESULT_OK) && requestCode == REQUEST_TAKE_PHOTO) {
            list.add(Uri.fromFile( File(currentPhotoPath)))
            imageAdapter?.notifyDataSetChanged()
            val uri:Uri? = data?.data

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
            if (uri!=null)
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
            //setPic(requestCode)

        }



    }

    private fun setPic(requestCode: Int) {
        // Get the dimensions of the View
        var imageView: ImageView = imageView_pic_create_prob

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
            images.add(currentPhotoPath)
            textView_tag_img_select.setText("Image select : " + images.size)
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

    private fun dispatchTakeGalleryPictureIntent(requestTakePhotoCode: Int) {
        /*startActivityForResult(
                Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ), requestTakePhotoCode
        )*/

        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestTakePhotoCode)

    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else if (count_dismiss < 3) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE), PERMISSION_REQUEST_CODE)
                    count_dismiss += 1
                } else openSetting()
                return

            }
        }


    }

    private fun openSetting() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Permissions Required")
                .setMessage(
                        "You have forcefully denied some of the required permissions " +
                                "for this action. Please open settings, go to permissions and allow them."
                )
                .setPositiveButton(
                        "Settings",
                        DialogInterface.OnClickListener { dialog, which ->
                            val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", packageName, null)
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        })
                .setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, which -> })
                .setCancelable(false)
                .create()
                .show()
    }
}