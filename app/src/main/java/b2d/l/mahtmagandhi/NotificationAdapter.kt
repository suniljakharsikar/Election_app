package b2d.l.mahtmagandhi

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_noti.view.*
import java.text.SimpleDateFormat

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
        if (model.image_url.length>0) {
            Glide.with(holder.itemView).load(Url.burl + model.image_url).into(holder.iv)
            holder.iv.visibility = View.VISIBLE

        }else
            holder.iv.visibility=View.GONE

        //10:20 AM January 22,  2020
        //2021-01-07 13:02:02
        val sdf = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
        val date = sdf.parse(model.created_at)
        val tdf = SimpleDateFormat("hh:mm a MMMM dd, yyyy")
        val datetim =tdf.format(date)

        holder.dateTimeTv.setText(datetim)

    }

    override fun getItemCount(): Int {
           return data.size
    }
}