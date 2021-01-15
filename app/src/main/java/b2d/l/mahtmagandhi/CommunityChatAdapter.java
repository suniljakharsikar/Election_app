package b2d.l.mahtmagandhi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityChatAdapter extends RecyclerView.Adapter<CommunityChatAdapter.ViewHolder> {
    private Context context;
    private List<ChatDataResponseModel.Data> chatData;
    private String s;
    private String s1;
    private String s2;
    private Boolean passToken;
    private AVLoadingIndicatorView avi;


    public CommunityChatAdapter(Context context, List<ChatDataResponseModel.Data> chatData, String s, String s1, String s2, boolean pass, AVLoadingIndicatorView avi) {

        this.context = context;
        this.chatData = chatData;
        this.s = s;
        this.s1 = s1;
        this.s2 = s2;
        this.passToken = pass;
        this.avi = avi;
    }

    @NonNull
    @Override
    public CommunityChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.raw_comunitytalk, parent, false);

//        contactView.setOnClickListener(i);
        // Return a new holder instance
        return new CommunityChatAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final ChatDataResponseModel.Data x = chatData.get(position);
        try {
            ChatDataResponseModel.Data.UserData userData = x.getUserData().get(0);
            holder.usernameTv.setText(userData.getUser_name());
            Glide.with(holder.itemView).load(userData.getUser_image()).into(holder.avtarIv);
        } catch (Exception e) {

        }

//        holder.imageView.setImageResource(x.getImage());
        holder.likesCountTv.setText(x.getLikes() + "");
        holder.dislikesCountTv.setText(x.getDislike() + "");
        holder.commentCountTv.setText(x.getCommentCount() + " Comment");
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Comment.class);
                intent.putExtra("pos", x.getId() + "");
                intent.putExtra("url", s1);
                intent.putExtra("newposturl", s2);
                intent.putExtra("passToken", passToken);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

        holder.commentCountTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                holder.comment.performClick();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.descTv.setText(Html.fromHtml(x.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.descTv.setText(Html.fromHtml(x.getDescription()));
        }

        List<ChatDataResponseModel.Data.ImageData> imgs = x.getImageData();
        String img = null;
        if (imgs != null && imgs.size() > 0) {
            img = Url.burl + imgs.get(0).getImage_name();
        }

        if (img == null && x.getImage_name() != null) {
            if (x.getImage_name() != null)
                img = x.getImage_name().toString();
        }
        if (img != null && img.contains("election"))
            if (!img.contains("https")) img = Url.http + img;
            else if (!img.contains("election")) img = Url.burl + img;

        if (img == null) holder.descIv.setVisibility(View.GONE);
        else holder.descIv.setVisibility(View.VISIBLE);

        Glide.with(context).load(img).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.descIv);

        String finalImg = img;
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = null;
                    if (finalImg != null && finalImg.length() > 0)
                        uri = Utility.INSTANCE.saveBitmap(
                                view.getContext(),
                                holder.descIv,
                                Bitmap.CompressFormat.JPEG,
                                "image/jpeg",
                                "",
                                "statement"
                        );

                    Utility.INSTANCE.share(Html.fromHtml(x.getDescription()).toString(), uri, view.getContext());

                } catch (IOException e) {

                }
//                Toast.makeText(context, "Sharing", Toast.LENGTH_SHORT).show();

            }
        });

        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (x.getUnlikeStatus() == 1) {
                    Toast.makeText(context, "you already disliked", Toast.LENGTH_SHORT).show();
                    return;
                }
                like_dislike(2, x, holder.likesCountTv, holder.dislikesCountTv, position);

            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (x.getLikeStatus() == 1) {
                    Toast.makeText(context, "you already liked", Toast.LENGTH_SHORT).show();
                    return;
                }
                like_dislike(1, x, holder.likesCountTv, holder.dislikesCountTv, position);

            }
        });

        String cmtCount = String.valueOf(x.getCommentCount());
        if (cmtCount != null) {
            holder.commentCountTv.setText(cmtCount + " Comment");
        } else
            holder.commentCountTv.setText("0 Comment");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date date = null;
        try {
            if (x.getCreated_at() != null)
                date = sdf.parse(x.getCreated_at());
            SimpleDateFormat tdf = new SimpleDateFormat("hh:mm a MMMM dd, yyyy");
            String datetim = tdf.format(date);

            holder.datetime.setText(datetim);
        } catch (Exception e) {

        }

        if (x.getLikeStatus() == 1) {
            holder.likeThumbIv.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.thumb_up));
            holder.likeThumbTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.thumb_up));

        } else {
            holder.likeThumbIv.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.dividercolor));
            holder.likeThumbTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.dividercolor));

        }

        if (x.getUnlikeStatus() == 1) {
            holder.disLikeThumbIv.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.thumb_down));
            holder.dislikeThumbTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.thumb_down));
        } else {
            holder.disLikeThumbIv.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.dividercolor));
            holder.dislikeThumbTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.dividercolor));
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void like_dislike(int i, ChatDataResponseModel.Data x, TextView likes, TextView dilikes, int position) {
        String url = Url.baseurl + s;
        JSONObject json = new JSONObject();
        try {
            json.put("parentId", x.getId());
            json.put("type", i);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (avi != null) {
            avi.setVisibility(View.VISIBLE);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        if (i == 1) {//like increase
                            x.setLikes(x.getLikes() + 1);
                            x.setLikeStatus(1);
                            likes.setText(x.getLikes() + "");
                            if (x.getUnlikeStatus() == 1) {
                                x.setDislike(x.getDislike() - 1);
                                x.setUnlikeStatus(0);
                                dilikes.setText(x.getDislike() + "");
                            }
                            if (avi != null)
                                stopanim();
                        }
                        if (i == 2) {
                            x.setDislike(x.getDislike() + 1);
                            x.setUnlikeStatus(1);
                            dilikes.setText(x.getDislike() + "");
                            if (x.getLikeStatus() == 1) {
                                x.setLikes(x.getLikes() - 1);
                                x.setLikeStatus(0);
                                likes.setText(x.getDislike() + "");
                            }
                            if (avi != null)
                                avi.setVisibility(View.INVISIBLE);

                        }
//                        Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        notifyItemChanged(position);
                        //// TODO: 19/12/20 refresh number of likes
                    } else {
                        Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        //login page
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.apply();
                        editor.clear();
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                stopanim();

                Toast.makeText(context, "" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                header.put("token", preferences.getString(Datas.token, ""));
                header.put("lid", preferences.getString(Datas.lagnuage_id, "1"));
                return header;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void stopanim() {
        avi.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return chatData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView descIv, avtarIv, likeThumbIv, disLikeThumbIv;
        TextView likesCountTv, likeThumbTv, dislikeThumbTv, commentBtnTv, shareCountTv, shareBtnTextView, dislikesCountTv, commentCountTv, usernameTv, descTv, datetime;
        LinearLayout dislike, like, comment, share;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descIv = itemView.findViewById(R.id.imageView_desc_commu_talk);
            avtarIv = itemView.findViewById(R.id.iv_avtar_commu_talk);
            likesCountTv = itemView.findViewById(R.id.textView_like_count);
            dislikesCountTv = itemView.findViewById(R.id.textView_disike_count);
            commentCountTv = itemView.findViewById(R.id.textView_count_comment);
            comment = itemView.findViewById(R.id.linearLayout_comment);
            share = itemView.findViewById(R.id.linearLayout_share);

            commentBtnTv = itemView.findViewById(R.id.textView_btn_comment_commu_talk);
            shareBtnTextView = itemView.findViewById(R.id.tv_btn_share);
            shareCountTv = itemView.findViewById(R.id.textView_count_share);
            usernameTv = itemView.findViewById(R.id.tv_name_commu_talk);
            descTv = itemView.findViewById(R.id.tv_des_commu_talk);
            dislike = itemView.findViewById(R.id.dislike);
            like = itemView.findViewById(R.id.like);
            datetime = itemView.findViewById(R.id.tv_time_commu_talk);
            likeThumbIv = itemView.findViewById(R.id.imageView53);
            disLikeThumbIv = itemView.findViewById(R.id.imageView49);
            likeThumbTv = itemView.findViewById(R.id.textView_btn_like_commu_talk);
            dislikeThumbTv = itemView.findViewById(R.id.textView_btn_dislike_commu_talk);


        }
    }
}
