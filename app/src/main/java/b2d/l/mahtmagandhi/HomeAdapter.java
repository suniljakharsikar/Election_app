package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private ArrayList<HomelistData> homelistDatas;
    private View.OnClickListener i = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);
            Intent intent = new Intent();
            switch (itemPosition) {
                case 0:
                    intent.setClass(context, AboutUs.class);
                    break;
                case 1:
                    intent.setClass(context, Meetings.class);
                    break;
                case 2:
                    intent.setClass(context, CommunityChat.class);
                    break;
                case 3:
                    intent.setClass(context, NewsUpdate.class);
                    break;
                case 4:
                    intent.setClass(context, OurWorks.class);
                    break;
                case 5:
                    intent.setClass(context, VisionMission.class);
                    break;
                case 6:
                    intent.setClass(context, ProblemSuggestion.class);
                    break;
                case 7:
                    intent.setClass(context, Manifesto.class);
                    break;
                case 8:
                    intent.setClass(context, Survey.class);
                    break;
                case 9:
                    intent.setClass(context, CommitteeMember.class);
                    break;
                case 10:
                    intent.setClass(context, VotingGuide.class);
                    break;
                case 11:
                    intent.setClass(context, Helpline.class);
                    break;
                case 12:
                    intent.setClass(context, SettingProfile.class);
                    break;
                default:
                    intent.setClass(context, Home.class);
                    break;
            }
            context.startActivity(intent);

        }
    };
    private RecyclerView mRecyclerView;

    public HomeAdapter(Context context, ArrayList<HomelistData> homelistDatas, RecyclerView recyclerView) {
        this.context = context;
        this.homelistDatas = homelistDatas;
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.home_raw, parent, false);

        contactView.setOnClickListener(i);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HomelistData homelistData = homelistDatas.get(position);

        holder.name.setText(homelistData.getMenuName());
        holder.icon.setImageResource(homelistData.getIconId());

    }

    @Override
    public int getItemCount() {
        return homelistDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView11);
            icon = itemView.findViewById(R.id.imageView9);
        }
    }
}
