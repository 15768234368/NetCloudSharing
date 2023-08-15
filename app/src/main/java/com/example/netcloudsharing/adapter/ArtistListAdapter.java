package com.example.netcloudsharing.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.Bean.ArtistBean;
import com.example.netcloudsharing.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder> {
    private static final String TAG = "ArtistListAdapter";
    private List<ArtistBean> mArtistList;
    private Context mContext;
    OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView setImage;
        TextView setName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setImage = (ImageView) itemView.findViewById(R.id.music_set_image);
            setName = (TextView) itemView.findViewById(R.id.music_set_name);
        }
    }
    public ArtistListAdapter(Context context, List<ArtistBean> mArtistList) {
        this.mContext = context;
        this.mArtistList = mArtistList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.music_set_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ArtistBean bean = mArtistList.get(position);


        new Thread(new Runnable() {
            @Override
            public void run() {
                getBitmapFromUrl(bean.getArtistPic(), new BitmapListener() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap) {
                        if (bitmap != null) {
                            // 在这里使用Bitmap对象更新UI组件
                            ((Activity) holder.itemView.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.setImage.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                });

                ((Activity) holder.itemView.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.setName.setText(bean.getArtistName());
                    }
                });
            }
        }).start();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击事件触发时，回调设置的监听器
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(view, position);
                }
            }
        });

        Log.d(TAG, "onBindViewHolder: " + bean.getArtistName());
    }

    @Override
    public int getItemCount() {
        return mArtistList.size();
    }
    public interface BitmapListener {
        void onBitmapLoaded(Bitmap bitmap);
    }
    /**
     * 获取网络图片并返回图片资源bitmap
     *
     * @param url
     * @param listener
     */
    public static void getBitmapFromUrl(final String url, final BitmapListener listener) {
        // 在新线程中执行网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 在这里执行网络请求，并获取Bitmap对象
                Bitmap bitmap = null;
                try {
                    URL imageUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    conn.setInstanceFollowRedirects(true);
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // 将Bitmap对象回调给监听器
                if (listener != null) {
                    listener.onBitmapLoaded(bitmap);
                }
            }
        }).start();
    }
}
