package b2d.l.mahtmagandhi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.lang3.StringEscapeUtils
import java.net.URLDecoder

class ProblemSuggestionRecyclerViewAdapter(private val data: MutableList<ProblemsResponseModel.Data>, val s1: String, val s2: String) : RecyclerView.Adapter<ProblemSuggestionRecyclerViewAdapter.ProblemSuggestionViewHolder>() {
    class ProblemSuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv = itemView.findViewById<TextView>(R.id.textView_title_prob)
        val desTv = itemView.findViewById<TextView>(R.id.textView_summary_prob)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemSuggestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_problem_suggestion, parent, false)
        return ProblemSuggestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProblemSuggestionViewHolder, position: Int) {
        val model = data.get(position)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, Comment::class.java)
            intent.putExtra("pos", model.id)
            intent.putExtra("url", s1)
            intent.putExtra("newposturl", s2)
            intent.putExtra("page_name", "Problem & Suggestion")

            if (model.isResolved==1)intent.putExtra("isResolved", true)
            else if (model.isResolved==0)intent.putExtra("isResolved", false)
            var x = model.imageArr

            intent.putExtra("imgs", x)
            intent.putExtra("dis", StringEscapeUtils.unescapeJava(model.description));
            intent.putExtra("title", StringEscapeUtils.unescapeJava(model.title))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
            //                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
            holder.itemView.context.startActivity(intent)
//                holder.itemView.context.startActivity(Intent(holder.itemView.context, ChatOnProblem::class.java))
        }
        holder.titleTv.text = StringEscapeUtils.unescapeJava(model.title)
        holder.desTv.text = StringEscapeUtils.unescapeJava(model.description)
        //holder.upTv.text = model.

    }

    override fun getItemCount(): Int {
        return data.size
    }
}