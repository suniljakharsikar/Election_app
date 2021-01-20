package b2d.l.mahtmagandhi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_of_img_pager.view.*

class ImagePagerAdapter(private val imgs: MutableList<ChatDataResponseModel.Data.ImageData>) : RecyclerView.Adapter<ImagePagerAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_of_img_pager,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

       Glide.with(holder.itemView.context).load(Url.burl+imgs.get(position).image_name).into(holder.itemView.item_image_view_pager)

    }

    override fun getItemCount(): Int {
        return imgs.size
    }
}