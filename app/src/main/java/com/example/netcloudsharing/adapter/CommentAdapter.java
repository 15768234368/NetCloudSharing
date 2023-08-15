package com.example.netcloudsharing.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.netcloudsharing.adapter.ArtistListAdapter.getBitmapFromUrl;

import com.example.netcloudsharing.Bean.CommentBean;
import com.example.netcloudsharing.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    List<CommentBean> mData;
    Context context;

    public CommentAdapter(List<CommentBean> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userImage_Iv;
        TextView userName_Tv, msg_Tv, time_Tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage_Iv = itemView.findViewById(R.id.item_comment_userImage);
            userName_Tv = itemView.findViewById(R.id.item_comment_userName);
            msg_Tv = itemView.findViewById(R.id.item_comment_msg);
            time_Tv = itemView.findViewById(R.id.item_comment_time);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CommentBean bean = mData.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getBitmapFromUrl(bean.getUserImage(), new ArtistListAdapter.BitmapListener() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap) {
                        if (bitmap != null) {
                            // 在这里使用Bitmap对象更新UI组件
                            ((Activity) holder.itemView.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.userImage_Iv.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                });

                ((Activity) holder.itemView.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.userName_Tv.setText(bean.getUserName());
                        holder.msg_Tv.setText(bean.getMsg());
                        holder.time_Tv.setText(bean.getTime());
                    }
                });
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
