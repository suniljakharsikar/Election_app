package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private final Context context;
    private ArrayList<HomelistData> homelistDatas;
    private RecyclerView mRecyclerView;

    public HomeAdapter(Context context, RecyclerView mRecyclerView, ArrayList<HomelistData> homelistDatas) {
        this.context =context;
        this.mRecyclerView = mRecyclerView;
        this.homelistDatas = homelistDatas;

    }

    private View.OnClickListener i = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.setSelected(true);
            new Handler().postDelayed(() -> view.setSelected(false), 100);
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == TYPE_ITEM) {

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.home_raw, parent, false);

            contactView.setOnClickListener(i);
            // Return a new holder instance
            return new VHItem(contactView);
        } else if (viewType == TYPE_FOOTER) {
            //inflate your layout and pass it to view holder

            View view = inflater.inflate(R.layout.item_appointment_btn,parent,false);

            return new VHHeader(view);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
            ((VHHeader) holder).appointmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    context.startActivity(new Intent(context,Appointment.class));
                }
            });
        }else if (holder instanceof VHItem){
          HomelistData homelistData = getItem(position);

          ((VHItem) holder).name.setText(homelistData.getMenuName());
          ((VHItem) holder).icon.setImageResource(homelistData.getIconId());

      }
    }

    @Override
    public int getItemCount() {
        return homelistDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == homelistDatas.size())
            return TYPE_FOOTER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private HomelistData getItem(int position) {
        return homelistDatas.get(position);
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;

        public VHItem(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView11);
            icon = itemView.findViewById(R.id.imageView9);
        }

    }

    class VHHeader extends RecyclerView.ViewHolder {
        Button appointmentButton;

        public VHHeader(View itemView) {
            super(itemView);
            appointmentButton = itemView.findViewById(R.id.button_appointment);
        }
    }
}