package b2d.l.mahtmagandhi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProblemSuggestionRecyclerViewAdapter(): RecyclerView.Adapter<ProblemSuggestionRecyclerViewAdapter.ProblemSuggestionViewHolder>() {
    class ProblemSuggestionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val titleTv = itemView.findViewById<TextView>(R.id.textView_title_prob)
        val desTv = itemView.findViewById<TextView>(R.id.textView_summary_prob)
        val upTv = itemView.findViewById<TextView>(R.id.textView_up_prob)
        val downTv = itemView.findViewById<TextView>(R.id.textView_down_prob)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemSuggestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_problem_suggestion,parent,false)
        return ProblemSuggestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProblemSuggestionViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
            return 10
    }
}