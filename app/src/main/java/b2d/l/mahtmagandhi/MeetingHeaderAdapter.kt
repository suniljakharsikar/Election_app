package b2d.l.mahtmagandhi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.raw_header_meeting.view.*
import java.text.SimpleDateFormat

class MeetingHeaderAdapter(private val date:String): RecyclerView.Adapter<MeetingHeaderAdapter.MyHeaderMeetingViewHolder>() {
    class MyHeaderMeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTv = itemView.textView_date_header_meeting
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHeaderMeetingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_header_meeting,parent,false);
        return MyHeaderMeetingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHeaderMeetingViewHolder, position: Int) {
        val sdf = SimpleDateFormat("yyyy-mm-dd")
        val d = sdf.parse(date)
        val tf = SimpleDateFormat("MMMM dd, yyyy")
        val td = tf.format(d)
        holder.dateTv.text = td
    }

    override fun getItemCount(): Int {
    return 1
    }
}