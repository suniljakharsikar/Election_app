package b2d.l.mahtmagandhi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.tntkhang.fullscreenimageview.library.FullScreenImageViewActivity
import kotlinx.android.synthetic.main.item_of_img_pager.view.*


class CommunityImagePagerAdapter(private val imgs: MutableList<ChatDataResponseModel.Data.ImageData>) : RecyclerView.Adapter<CommunityImagePagerAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_of_img_pager, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.itemView.setOnClickListener {
            // val url: String =Url.burl +  imgs.get(position).image_name
            if (imgs.size > 0) {
                val fullImageIntent = Intent(holder.itemView.context, FullScreenImageViewActivity::class.java)
// uriString is an ArrayList<String> of URI of all images
                // uriString is an ArrayList<String> of URI of all images
                val list = arrayListOf<String>()
                for (i in imgs) {
                    list.add(Url.burl + i.image_name)
                }
                fullImageIntent.putExtra(FullScreenImageViewActivity.URI_LIST_DATA, list)
// pos is the position of image will be showned when openr
                // pos is the position of image will be showned when open
                fullImageIntent.putExtra(FullScreenImageViewActivity.IMAGE_FULL_SCREEN_CURRENT_POS, position)
                holder.itemView.context.startActivity(fullImageIntent)
            }
        }
        val iv = holder.itemView.item_image_view_pager
        val bgIv = holder.itemView.item_image_bg


        Glide.with(holder.itemView.context)
                .asBitmap()
                .load(Url.burl + imgs.get(position).image_name)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                        iv.setImageBitmap(resource)

                        val blurred = blurRenderScript(holder.itemView.context, resource, 25)

                        bgIv.setImageBitmap(blurred)
                        //setBackgroundColor(Utility.createPaletteSync(resource).darkMutedSwatch,iv)

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })




    }


    @SuppressLint("NewApi")
    fun blurRenderScript(context: Context?, smallBitmap: Bitmap, radius: Int): Bitmap? {
        var smallBitmap = smallBitmap
        try {
            smallBitmap = RGB565toARGB888(smallBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val bitmap = Bitmap.createBitmap(
                smallBitmap.width, smallBitmap.height,
                Bitmap.Config.ARGB_8888)
        val renderScript = RenderScript.create(context)
        val blurInput = Allocation.createFromBitmap(renderScript, smallBitmap)
        val blurOutput = Allocation.createFromBitmap(renderScript, bitmap)
        val blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript))
        blur.setInput(blurInput)
        blur.setRadius(radius.toFloat()) // radius must be 0 < r <= 25
        blur.forEach(blurOutput)
        blurOutput.copyTo(bitmap)
        renderScript.destroy()
        return bitmap
    }

    @Throws(Exception::class)
    private fun RGB565toARGB888(img: Bitmap): Bitmap {
        val numPixels = img.width * img.height
        val pixels = IntArray(numPixels)

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.width, 0, 0, img.width, img.height)

        //Create a Bitmap of the appropriate format.
        val result = Bitmap.createBitmap(img.width, img.height, Bitmap.Config.ARGB_8888)

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
        return result
    }
    override fun getItemCount(): Int {
        return imgs.size
    }
}