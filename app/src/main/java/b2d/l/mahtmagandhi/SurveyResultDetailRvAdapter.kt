package b2d.l.mahtmagandhi

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_of_survey.view.*

class SurveyResultDetailRvAdapter(val optionsData: List<SurveyResponseModifyModel.Data.OptionsData>, val answeredOptionId: Int) : RecyclerView.Adapter<SurveyResultDetailRvAdapter.SurveyResultViewHolder>() {
    class SurveyResultViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val optionTextView = itemView.tv_option_survey
        val perTextView = itemView.tv_per_survey_result

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_of_survey,parent,false)
        return SurveyResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SurveyResultViewHolder, position: Int) {
        val model = optionsData.get(position)
        holder.optionTextView.text = model.options_data
        holder.perTextView.text = model.answer_per

        if (answeredOptionId==model.id)
        ViewCompat.setBackgroundTintList(
                holder.itemView,
                ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context,R.color.success)));
        else
            ViewCompat.setBackgroundTintList(
                    holder.itemView,
                    ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context,R.color.grey_light)));




    }

    override fun getItemCount(): Int {
        return  optionsData.size
    }
}