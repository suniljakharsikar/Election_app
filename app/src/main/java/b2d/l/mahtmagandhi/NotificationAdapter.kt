package b2d.l.mahtmagandhi

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_noti.view.*

class NotificationAdapter(val data: List<NotificationResponseModel.Data>) : RecyclerView.Adapter<NotificationAdapter.MyNotificationViewHolder>() {
    class MyNotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv = itemView.textView_title_noti_head
        val descTv = itemView.textView_desc_noti
        val dateTimeTv = itemView.textView_date_time_noti
        val iv = itemView.imageView_noti

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNotificationViewHolder {
    val view =LayoutInflater.from(parent.context).inflate(R.layout.raw_noti, parent, false)
    return MyNotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyNotificationViewHolder, position: Int) {

        val model = data.get(position)
        holder.titleTv.text = model.notification

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.descTv.text = (Html.fromHtml(model.description, Html.FROM_HTML_MODE_COMPACT))
        } else {
            holder.descTv.text = Html.fromHtml(model.description)
        }
        Glide.with(holder.itemView).load(Url.burl + model.image_url).into(holder.iv)

    }

    override fun getItemCount(): Int {
           return data.size
    }
}