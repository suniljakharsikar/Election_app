package b2d.l.mahtmagandhi

import android.app.Activity
import android.content.*
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.view.drawToBitmap
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


object Utility {

    // Generate palette synchronously and return it

    // Generate palette asynchronously and use it on a different
// thread using onGenerated()


    fun fullScreenImage(){

    }    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getPath(uri: Uri?, context: Context): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = context.getContentResolver().query(uri!!, projection, null, null, null)
                ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }

    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                        split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
                column
        )
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs,
                    null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
    @NonNull
    @Throws(IOException::class)
    fun saveBitmap(
            @NonNull context: Context, @NonNull image: ImageView,
            @NonNull format: Bitmap.CompressFormat, @NonNull mimeType: String,
            @NonNull displayName: String, @Nullable subFolder: String
    ): Uri? {
        if (image.width > 0 && image.height > 0) {
            val bitmap = image.drawToBitmap(Bitmap.Config.ARGB_8888)
            var relativeLocation: String = Environment.DIRECTORY_PICTURES
            if (subFolder.isNotEmpty()) {
                relativeLocation += File.separator + subFolder
            }
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
            }
            val resolver: ContentResolver = context.contentResolver
            var stream: OutputStream? = null
            var uri: Uri? = null
            return try {
                val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                uri = resolver.insert(contentUri, contentValues)
                if (uri == null) {
                    throw IOException("Failed to create new MediaStore record.")
                }
                stream = resolver.openOutputStream(uri)
                if (stream == null) {
                    throw IOException("Failed to get output stream.")
                }
                if (bitmap.compress(format, 95, stream) == false) {
                    throw IOException("Failed to save bitmap.")
                }
                uri
            } catch (e: IOException) {
                if (uri != null) {
                    // Don't leave an orphan entry in the MediaStore
                    resolver.delete(uri, null, null)
                }
                throw e
            } finally {
                if (stream != null) {
                    stream.close()
                }
            }
        }else return null
    }

    fun share(detail: String, uri: Uri?, context: Context){

        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND

            //putExtra(Intent.EXTRA_TITLE, "eGram pay : $mobile account information.")
            // putExtra(Intent.EXTRA_SUBJECT, "eGram pay : $mobile account information.")

            putExtra(
                    Intent.EXTRA_TEXT, detail

            )


            // (Optional) Here we're setting the title of the content
            // putExtra(Intent.EXTRA_SUBJECT, "eGram pay : $mobile account information.")
            if (uri != null) {
                this.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                putExtra(Intent.EXTRA_STREAM, uri)
            }
            type = "text/html"
            // (Optional) Here we're passing a content URI to an image to be displayed
            // data = uri
            //flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }, null)

        context.startActivity(share)

    }

    fun getLocalBitmapUri(bmp: Bitmap, context: Context): Uri? {
        var bmpUri: Uri? = null
        try {
            val file: File = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png")
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }


    fun customSnackBar(view: View, context: Context, text: String, color: Int, res: Int){
        // Create the Snackbar

        // Create the Snackbar
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
// Get the Snackbar's layout view
// Get the Snackbar's layout view
        val layoutv = snackbar.view as FrameLayout
        val params = layoutv.layoutParams as FrameLayout.LayoutParams

        params.setMargins(0, dpIntoPx(72f,context).toInt(), 0, 0)
        params.gravity = Gravity.TOP
        layoutv.layoutParams = params
// Hide the text
// Hide the text
        val textView = layoutv.findViewById<View>(R.id.snackbar_text) as TextView
        textView.visibility = View.INVISIBLE

// Inflate our custom view

// Inflate our custom view
        val snackView: View = LayoutInflater.from(context).inflate(R.layout.my_snackbar, null)
// Configure the view
// Configure the view

        val imageView = snackView.findViewById<View>(R.id.imageView_status_snackbar) as ImageView
        imageView.setImageResource(res)
        val textViewTop = snackView.findViewById<View>(R.id.textView_status_snackbar) as TextView
        textViewTop.setText(text)
        textViewTop.setTextColor(color)

//If the view is not covering the whole snackbar layout, add this line

//If the view is not covering the whole snackbar layout, add this line
        layoutv.setPadding(0, 0, 0, 0)


// Add the view to the Snackbar's layout

// Add the view to the Snackbar's layout
        layoutv.addView(snackView, 0)
// Show the Snackbar
// Show the Snackbar
        snackbar.show()
    }


    fun dpIntoPx(float:Float,context: Context):Float {
        val dip = float
        val r: Resources = context.getResources()
        val px: Float = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        )
        return px

    }

}