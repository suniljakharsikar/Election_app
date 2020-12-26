package b2d.l.mahtmagandhi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProblemSuggestionRecyclerViewAdapter(private val data: MutableList<ProblemsResponseModel.Data>) : RecyclerView.Adapter<ProblemSuggestionRecyclerViewAdapter.ProblemSuggestionViewHolder>() {
    class ProblemSuggestionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val titleTv = itemView.findViewById<TextView>(R.id.textView_title_prob)
        val desTv = itemView.findViewById<TextView>(R.id.textView_summary_prob)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemSuggestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_problem_suggestion,parent,false)
        return ProblemSuggestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProblemSuggestionViewHolder, position: Int) {
        val model = data.get(position)
            holder.itemView.setOnClickListener {
                holder.itemView.context.startActivity(Intent(holder.itemView.context,ChatOnProblem::class.java))
            }
        holder.titleTv.text = model.title
        holder.desTv.text = model.description
        //holder.upTv.text = model.

    }

    override fun getItemCount(): Int {
            return data.size
    }
}