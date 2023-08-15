package com.example.netcloudsharing.User;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netcloudsharing.Bean.ArtistBean;
import com.example.netcloudsharing.adapter.ArtistListAdapter;
import com.example.netcloudsharing.adapter.LocalMusicAdapter;
import com.example.netcloudsharing.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MySingerAdapter extends RecyclerView.Adapter<MySingerAdapter.ArtistViewHolder> {
    private static final String TAG = "MySingerAdapter";
    Context context;
    List<ArtistBean> mDatas;
    LocalMusicAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(LocalMusicAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MySingerAdapter(Context context, List<ArtistBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder {
        ImageView artistPic;
        TextView name, artistFans, musicNum, albumNum, mvNum, aartist, country, info, allInfo;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistPic = itemView.findViewById(R.id.artist_homePage_image);
            name = itemView.findViewById(R.id.artist_homePage_name);
            artistFans = itemView.findViewById(R.id.artist_homePage_artistFans);
            musicNum = itemView.findViewById(R.id.artist_homePage_musicNum);
            albumNum = itemView.findViewById(R.id.artist_homePage_albumNum);
            mvNum = itemView.findViewById(R.id.artist_homePage_mvNum);
            aartist = itemView.findViewById(R.id.artist_homePage_aartist);
            country = itemView.findViewById(R.id.artist_homePage_country);
            info = itemView.findViewById(R.id.artist_homePage_info);
            allInfo = itemView.findViewById(R.id.artist_homePage_allInfo);

        }
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mysinger, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArtistViewHolder holder, final int position) {
        final ArtistBean bean = mDatas.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getBitmapFromUrl(bean.getArtistPic(), new ArtistListAdapter.BitmapListener() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap) {
                        if (bitmap != null) {
                            // 在这里使用Bitmap对象更新UI组件
                            ((Activity) holder.itemView.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.artistPic.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                });

                ((Activity) holder.itemView.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.name.setText(bean.getArtistName());
                        holder.artistFans.setText("粉丝：" + bean.getArtistFans());
                        holder.musicNum.setText("单曲：" + bean.getMusicNum());
                        holder.albumNum.setText("专辑：" + bean.getAlbumNum());
                        holder.mvNum.setText("MV：" + bean.getMvNum());
                        holder.aartist.setText("英文名：" + bean.getAartist());
                        holder.country.setText("国籍：" + bean.getCountry());
                        holder.info.setText("个人简介：" + bean.getInfo());
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
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    /**
     * 获取网络图片并返回图片资源bitmap
     *
     * @param url
     * @param listener
     */
    public static void getBitmapFromUrl(final String url, final ArtistListAdapter.BitmapListener listener) {
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
