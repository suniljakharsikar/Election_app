package b2d.l.mahtmagandhi

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_create_problem_and_suggestion.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreateProblemAndSuggestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_problem_and_suggestion)
        tv_image_btn_prob_sug.setOnClickListener {
            val dialog = ImagePickerBottomSheetDialogFragment()
            dialog.show(supportFragmentManager, "pic")
        }

    }

    fun back(view: View) {
        finish()
    }

    fun isNullOrEmpty(str: String?): Boolean {
        return if (str != null && !str.isEmpty()) false else true
    }

    fun submit(view: View) {

        if (et_prob_sugg.text.isEmpty()) Toast.makeText(this, "Please write your problem and suggestion.", Toast.LENGTH_SHORT).show()
        else
        {

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
                            "com.lotu.egrampay.fileprovider",
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

    private  var currentPath: String = ""
    private val REQUEST_TAKE_GPHOTO: Int = 51
    private val REQUEST_TAKE_PHOTO: Int = 50
    lateinit var currentPhotoPath: String

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
        if (resultCode.equals(Activity.RESULT_OK) && requestCode >5){
            val uri = data!!.data

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
            currentPhotoPath = PathUtil.getPath(this, uri)

            var result: Bitmap? =  BitmapFactory.decodeFile(currentPhotoPath)
            if (result==null){
                val file = File(uri!!.path) //create path from uri
                if (file.path.contains(":")) {
                    val split = file.path.split(":".toRegex()).toTypedArray() //split the path.

                    currentPhotoPath = split[1]
                }else currentPhotoPath = file.absolutePath
                result =    BitmapFactory.decodeFile(currentPhotoPath)
                if (result==null){
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor: Cursor? =getContentResolver()?.query(
                            uri,
                            filePathColumn, null, null, null
                    )
                    cursor?.moveToFirst()

                    val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
                    currentPhotoPath = columnIndex?.let { cursor?.getString(it) }.toString()
                    cursor?.close()
                }

                result =    BitmapFactory.decodeFile(currentPhotoPath)


            }

            //   BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?
            //}


        }
        setPic(requestCode)


    }

    private fun setPic(requestCode: Int) {
        // Get the dimensions of the View
        var imageView: ImageView = imageView_pic_create_new_prob_sug

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
        if (s.equals("c")){
            when(tag){
                "pic" -> dispatchTakePictureIntent(REQUEST_TAKE_PHOTO)

            }
        }else{
            when(tag){
                "pic" -> dispatchTakeGalleryPictureIntent(REQUEST_TAKE_GPHOTO)

            }
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