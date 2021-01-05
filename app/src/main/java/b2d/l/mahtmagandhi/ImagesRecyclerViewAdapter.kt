package b2d.l.mahtmagandhi

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_of_imgs.view.*

class ImagesRecyclerViewAdapter(val imagesEncodedList: MutableList<Uri>) : RecyclerView.Adapter<ImagesRecyclerViewAdapter.ImagesRecyclerViewHolder>() {

    class ImagesRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv = itemView.imageView29
        val removeIv = itemView.imageView_remove

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesRecyclerViewHolder {
val v = LayoutInflater.from(parent.context).inflate(R.layout.item_of_imgs,parent,false)
return ImagesRecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: ImagesRecyclerViewHolder, position: Int) {


        Glide.with(holder.itemView.context).load(imagesEncodedList?.get(position)).into(holder.iv)

        holder.removeIv.setOnClickListener {
            try {
                imagesEncodedList?.removeAt(position)
                notifyItemRemoved(position)
            } catch (e: Exception) {
            }
        }
    }

    override fun getItemCount(): Int {

        return imagesEncodedList!!.size

    }
}