package com.example.netcloudsharing.diary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.netcloudsharing.R;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private List<NoteInfo> data;

    MyAdapter(Context context, List<NoteInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.diary_list_item, parent, false);
            holder = new ViewHolder();
            holder.tv_listContent = convertView.findViewById(R.id.list_tvContent);
            holder.tv_listTime = convertView.findViewById(R.id.list_tvTime);
            holder.iv_listImage = convertView.findViewById(R.id.list_ivImg);
            holder.iv_listVideo = convertView.findViewById(R.id.list_ivVideo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_listContent.setText(data.get(position).getContent());
        holder.tv_listTime.setText(data.get(position).getTime());
        holder.iv_listImage.setImageBitmap(getImageThumbnail(data.get(position).getPath(),
                200, 200));
        return convertView;
    }

    /**
     * 获取缩略图的方法
     *
     * @param uri    路径
     * @param width  宽
     * @param height 高
     * @return 缩略图
     */
    public Bitmap getImageThumbnail(String uri, int width, int height) {
        Bitmap bitmap = null;
        //获取缩略图
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth / width;
        int beHeight = options.outHeight / height;
        int be = 1 ;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}

class ViewHolder {
    public TextView tv_listContent, tv_listTime;
    public ImageView iv_listImage, iv_listVideo;
}
