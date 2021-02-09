package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private final Context context;
    private List<MenusResponseModel.Data> homelistDatas;
    private RecyclerView mRecyclerView;

    private Home home ;
    public HomeAdapter(Context context, RecyclerView mRecyclerView, List<MenusResponseModel.Data> homelistDatas,Home home) {
        this.context =context;
        this.mRecyclerView = mRecyclerView;
        this.homelistDatas = homelistDatas;
        this.home = home;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == TYPE_ITEM) {

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.home_raw, parent, false);

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
            ((VHHeader) holder).logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    context.startActivity(new Intent(context, LoginActivity.class));
                    home.finish();


                }
            });
        }else if (holder instanceof VHItem){
          MenusResponseModel.Data model = getItem(position);
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  view.setSelected(true);
                  new Handler().postDelayed(() -> view.setSelected(false), 100);
                  int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                  Intent intent = new Intent();
                  switch (model.getMenu()) {
                      case "About Us":
                          intent.setClass(context, AboutUs.class);
                          break;
                      case "Appointment":
                          intent.setClass(context, Appointment.class);
                          break;

                      case "Community Chat":
                          intent.setClass(context, CommunityChat.class);
                          break;
                      case "News Update":
                          intent.setClass(context, NewsUpdate.class);
                          break;
                      case "Our Work":
                          intent.setClass(context, OurWorks.class);
                          break;
                      case "Vision Mission":
                          intent.setClass(context, VisionMission.class);
                          break;
                      case "Problem Suggestion":
                          intent.setClass(context, ProblemSuggestion.class);
                          break;
                      case "Manifesto":
                          intent.setClass(context, Manifesto.class);
                          break;
                      case "Survey":
                          intent.setClass(context, Survey.class);
                          break;
                      case "Committee Members":
                          intent.setClass(context, CommitteeMember.class);
                          break;
                      case "Voating Guide":
                          intent.setClass(context, VotingGuide.class);
                          break;
                      case "Helpline":
                          intent.setClass(context, Helpline.class);
                          break;
                      case "Setting Profile":
                          intent.setClass(context, SettingProfile.class);
                          break;

                      case "Meeting":
                          intent.setClass(context, Meetings.class);
                          break;

                      default: {
                          if (model.getType()==2){
                          intent.putExtra("url",model.getUrl());
                          intent.putExtra("title",model.getMenu());

                          intent.setClass(context, WebViewActivity.class);
                          }
                          else
                              intent.setClass(context, Home.class);

                          break;
                      }
                  }
                  context.startActivity(intent);

              }
          });




          ((VHItem) holder).name.setText(model.getMenu());
          if (model.getIcon().length()>0) {
              Glide.with(context).load(Url.burl+model.getIcon()).into(((VHItem) holder).icon);
          }else{

              int drawableRes = R.drawable.info;
              switch (model.getMenu()) {
                  case "About Us":
                      drawableRes = R.drawable.info;
                      break;
                  case "Appointment":
                      drawableRes = R.drawable.suitcase;
                      break;

                  case "Community Chat":
                      drawableRes = R.drawable.chatboxes;
                      break;
                  case "News Update":
                      drawableRes = R.drawable.newspaper;
                      break;
                  case "Our Work":
                      drawableRes = R.drawable.groupwork;
                      break;
                  case "Vision Mission":
                      drawableRes = R.drawable.vision;
                      break;
                  case "Problem Suggestion":
                      drawableRes = R.drawable.problem;
                      break;
                  case "Manifesto":
                      drawableRes = R.drawable.menifesto;
                      break;
                  case "Survey":
                      drawableRes = R.drawable.sarve;
                      break;
                  case "Committee Members":
                      drawableRes = R.drawable.commetee;
                      break;
                  case "Voating Guide":
                      drawableRes = R.drawable.vote;
                      break;
                  case "Helpline":
                      drawableRes = R.drawable.helpline;
                      break;
                  case "Setting Profile":
                      drawableRes = R.drawable.setting;
                      break;

                  case "Meeting":
                      drawableRes = R.drawable.suitcase;
                      break;


              }
              Glide.with(context).load(drawableRes).into(((VHItem) holder).icon);

          }



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

    private MenusResponseModel.Data getItem(int position) {
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
        Button logoutButton;

        public VHHeader(View itemView) {
            super(itemView);
            logoutButton = itemView.findViewById(R.id.button_logout);
        }
    }
}