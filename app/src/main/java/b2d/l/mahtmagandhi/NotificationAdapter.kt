package b2d.l.mahtmagandhi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.raw_noti.view.*

class NotificationAdapter: RecyclerView.Adapter<NotificationAdapter.MyNotificationViewHolder>() {
    class MyNotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv = itemView.textView_title_noti_head
        val descTv = itemView.textView_desc_noti
        val dateTimeTv = itemView.textView_date_time_noti


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNotificationViewHolder {
    val view =LayoutInflater.from(parent.context).inflate(R.layout.raw_noti,parent,false)
    return MyNotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyNotificationViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
           return 10
    }
}