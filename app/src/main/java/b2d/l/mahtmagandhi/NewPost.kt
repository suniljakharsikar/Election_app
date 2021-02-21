package b2d.l.mahtmagandhi

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.coroutines.*
import okhttp3.*
import java.io.File
import java.io.IOException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*


class NewPost : AppCompatActivity() {
    private  var imageAdapter: ImagesRecyclerViewAdapter? =null
    var editText: EditText? = null
    private var avi: ProgressBar? = null
    val list = mutableListOf<Uri>()

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
        setContentView(R.layout.activity_new_post)
        avi = avi4
        stopAnim()
        editText = findViewById(R.id.et_prob_sugg)
        imageAdapter = ImagesRecyclerViewAdapter(list)
        rv_imgs_new_post.adapter =imageAdapter

        button_add_img_new_post.setOnClickListener {
            addImage();
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
        rv_imgs_new_post.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



    }

    private fun addImage() {
        val dialog = ImagePickerBottomSheetDialogFragment()
        dialog.show(supportFragmentManager, "pic")
    }

    fun back(view: View?) {
       // Toast.makeText(this, "post not implemented", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun newposting(view: View?) {
        val s = editText!!.text.toString().trim()
        if (isNullOrEmpty(s)) {
            Toast.makeText(this, "Please write something before post", Toast.LENGTH_SHORT).show()
        } else {


            val job = GlobalScope.async {
                return@async Utility.isInternetAvailable()
            }
            job.invokeOnCompletion {
                val isInternet = job.getCompleted()
                GlobalScope.launch(Dispatchers.Main) {
                    if (isInternet) {
                        postToServer(s)

                    } else {
                        stopAnim()
                        Utility.customSnackBar(rv_imgs_new_post!!, this@NewPost, "No Internet Available.", ContextCompat.getColor(this@NewPost, R.color.error), R.drawable.ic_error)
                    }
                }
            }
        }



    }

    private fun postToServer(s: String) {
        startAnim()
        val client = OkHttpClient().newBuilder()
                .build()
        val mediaType = MediaType.parse("text/plain")
        val bodyP = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("postData", URLEncoder.encode(s, "UTF-8"))
        var counter = 0
        for (i in imageAdapter!!.imagesEncodedList){
            bodyP.addFormDataPart("postImage[" + counter + "]", "p.jpg", RequestBody.create(MediaType.parse("application/octet-stream"), File(Utility.getPath(this, i)!!)))
            counter = counter +1 ;
        }


        val body = bodyP.build()
        val preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)

        val request: Request = Request.Builder()
                .url(Url.baseurl + "/ctalk_post")
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
                    // imageView_new_post.setImageDrawable(null)
                    et_prob_sugg.setText("")
                    imageAdapter!!.imagesEncodedList.clear()
                    imageAdapter!!.notifyDataSetChanged()
                    //  Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                    stopAnim()
                    val intent = intent
                    setResult(1010, intent)
                    intent.putExtra("reload", true)

                    finish()
                }
            }
            stopAnim()
        }


    }


    companion object {
        fun isNullOrEmpty(str: String?): Boolean {
            return str == null || str.isEmpty()
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


        if(requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data?.getClipData() != null) {
                    var count = data!!.getClipData()!!.getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                  //  Toast.makeText(this, "" + count + "", Toast.LENGTH_SHORT).show()

                    for (i in 0..count - 1) {
                        val imageUri = data!!.getClipData()!!.getItemAt(i).getUri();
                        list.add(imageUri)
                    }
                    // Glide.with(this).load(data!!.getClipData()!!.getItemAt(0).getUri()).into(imageView_new_post)
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
        }else if(requestCode== 50){

            list.add(Uri.fromFile(File(currentPhotoPath)))
        // Glide.with(this).load(data!!.getClipData()!!.getItemAt(0).getUri()).into(imageView_new_post)
        imageAdapter?.notifyDataSetChanged()
        }


        super.onActivityResult(requestCode, resultCode, data)
    }    /*try {
            if (resultCode.equals(Activity.RESULT_OK)) {

                var uri = data?.data


                *//*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    // Do something for lollipop and above versions
                    val file = File(uri!!.path) //create path from uri
                     if (file.path.contains(":")) {
                         val split = file.path.split(":".toRegex()).toTypedArray() //split the path.

                         currentPhotoPath = split[1]
                     }else{
                         currentPhotoPath =file.path
                     }
                } else{*//*
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
        } catch (e: Exception) {
        }
        setPic(requestCode)*/




    private fun setPic(requestCode: Int) {
        // Get the dimensions of the View
        var imageView: ImageView = ImageView(this)
        //imageView_new_post.visibility = View.VISIBLE
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

    var PICK_IMAGE_MULTIPLE = 1
    var imageEncoded: String? = null
    var imagesEncodedList: java.util.ArrayList<String>? = arrayListOf()

    val OPEN_MEDIA_PICKER = 21

    private fun dispatchTakeGalleryPictureIntent(requestTakePhotoCode: Int) {

        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    fun addimage(view: View) {
        //callm()
    }

}




