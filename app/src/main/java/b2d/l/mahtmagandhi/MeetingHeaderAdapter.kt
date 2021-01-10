package b2d.l.mahtmagandhi

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.raw_header_meeting.view.*
import java.text.SimpleDateFormat

class MeetingHeaderAdapter(private val date: String, val timeHeading: TextView): RecyclerView.Adapter<MeetingHeaderAdapter.MyHeaderMeetingViewHolder>() {
    class MyHeaderMeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTv = itemView.textView_date_header_meeting
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHeaderMeetingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_header_meeting,parent,false)
        return MyHeaderMeetingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHeaderMeetingViewHolder, position: Int) {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val d = sdf.parse(date)
        Log.d("MeetingHeader", "onBindViewHolder: "+d.toString())
        val headFormat = SimpleDateFormat("MMMM yyyy")

        val tf = SimpleDateFormat("MMMM dd, yyyy")
        val head = headFormat.format(d)
        val td = tf.format(d)
        Log.d("MeetingHeader", "onBindViewHolder: "+head)
        holder.dateTv.text = td
        timeHeading.text = head

    }

    override fun getItemCount(): Int {
    return 1
    }
}