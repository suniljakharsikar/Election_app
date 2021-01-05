package b2d.l.mahtmagandhi

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.view.drawToBitmap
import java.io.File
import java.io.IOException
import java.io.OutputStream


object Utility {

    fun hideKeyboard(activity: Activity) {
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
            if (uri != null)
                putExtra(Intent.EXTRA_STREAM, uri)
            type = "text/html"
            // (Optional) Here we're passing a content URI to an image to be displayed
            // data = uri
            //flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }, null)
        context.startActivity(share)

    }}