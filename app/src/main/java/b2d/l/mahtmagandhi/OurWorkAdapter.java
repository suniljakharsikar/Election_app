package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OurWorkAdapter extends RecyclerView.Adapter<OurWorkAdapter.ViewHolder> {
    private List<OurWorkResponseModel.Data> data;

    public OurWorkAdapter( List<OurWorkResponseModel.Data> data) {

        this.data = data;
    }

    @NonNull
    @Override
    public OurWorkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.raw_our_work, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final OurWorkResponseModel.Data ourWorkData = data.get(position);
        holder.title.setText(ourWorkData.getTitle());
       // holder.content.setText(ourWorkData.getDescription());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.content.setText(Html.fromHtml(ourWorkData.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.content.setText(Html.fromHtml(ourWorkData.getDescription()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent d = new Intent(holder.itemView.getContext(), OurWorkDetail.class);
                d.putExtra("data", ourWorkData);

                holder.itemView.getContext().startActivity(d);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView20);
            content = itemView.findViewById(R.id.textView21);
        }
    }
}
