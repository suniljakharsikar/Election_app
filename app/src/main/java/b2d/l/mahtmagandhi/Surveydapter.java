package b2d.l.mahtmagandhi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Surveydapter extends RecyclerView.Adapter<Surveydapter.ViewHolder> {
    private Context context;
    private ArrayList<String> homelistDatas;
    private RecyclerView recyclerView;

    public Surveydapter(Context context, ArrayList<String> homelistDatas, RecyclerView recyclerView) {
        this.context = context;
        this.homelistDatas = homelistDatas;
        this.recyclerView = recyclerView;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String s = homelistDatas.get(position);
        holder.textView.setText(s);
    }


    @Override
    public int getItemCount() {
        return homelistDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView31);
        }
    }
}
