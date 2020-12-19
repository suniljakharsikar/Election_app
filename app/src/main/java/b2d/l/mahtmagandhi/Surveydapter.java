package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Surveydapter extends RecyclerView.Adapter<Surveydapter.ViewHolder> {
    List<SurveyResponseModel.Data> data;

    public Surveydapter(List<SurveyResponseModel.Data> data) {
        this.data = data;

    }


    @NonNull
    @Override
    public Surveydapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.survey_raw, parent, false);

//        contactView.setOnClickListener(i);
        // Return a new holder instance
        return new Surveydapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        String s = data.get(position).getTitle();
        holder.textView.setText(s);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(),SurveyDetailActivity.class));
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView31);
        }
    }
}
